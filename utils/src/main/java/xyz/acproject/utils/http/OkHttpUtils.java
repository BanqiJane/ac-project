package xyz.acproject.utils.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import xyz.acproject.utils.http.config.OkHttpConfig;
import xyz.acproject.utils.http.data.FormFileData;
import xyz.acproject.utils.http.type.Utf8MediaType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName OkHttpUtils
 * @Description TODO
 * @date 2022/7/1 16:25
 * @Copyright:2022
 */
public class OkHttpUtils {

    private static final Logger LOGGER = LogManager.getLogger(OkHttpUtils.class);
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, Object> headerMap;
    private Map<String, Object> paramMap;
    private String url;
    private Request.Builder request;
    private boolean log = false;

    private static final int READ_TIMEOUT = 15;
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtils() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectionPool(pool());
                    builder.sslSocketFactory(createSSLSocketFactory(buildTrustManagers()), buildX509TrustManager());
                    builder.hostnameVerifier((hostName, session) -> true);
                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                }
            }
        }
    }

    private OkHttpUtils(OkHttpConfig okHttpConfig) {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(okHttpConfig.READ_TIMEOUT(), TimeUnit.SECONDS);
                    builder.connectTimeout(okHttpConfig.CONNECT_TIMEOUT(), TimeUnit.SECONDS);
                    builder.writeTimeout(okHttpConfig.WRITE_TIMEOUT(), TimeUnit.SECONDS);
                    builder.connectionPool(okHttpConfig.pool());
                    this.log = okHttpConfig.LOGGER();
                    okHttpClient = builder.build();
                }
            }
        }
    }



    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtils.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static OkHttpUtils builder() {
        return new OkHttpUtils();
    }


    public static OkHttpUtils builder(OkHttpConfig okHttpConfig) {
        return new OkHttpUtils(okHttpConfig);
    }




    /**
     * 连接池 复用http
     */
    private ConnectionPool pool() {
        return new ConnectionPool(50, 5, TimeUnit.MINUTES);
    }



    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtils url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpUtils log() {
        this.log = true;
        return this;
    }

    public OkHttpUtils log(boolean enable) {
        this.log = enable;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addParam(String key, Object value) {
        if (value != null) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(16);
            }
            paramMap.put(key, value);
        }
        return this;
    }

    public OkHttpUtils params(Map<String, Object> paramMapNew){
        if (!CollectionUtils.isEmpty(paramMapNew)) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(16);
            }
            paramMap.putAll(paramMapNew);
        }
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addHeader(String key, Object value) {
        if (value != null) {
            if (headerMap == null) {
                headerMap = new LinkedHashMap<>(16);
            }
            headerMap.put(key, value);
        }
        return this;
    }

    public OkHttpUtils headers(Map<String, Object> headerMapNew){
        if (!CollectionUtils.isEmpty(headerMapNew)) {
            if (headerMap == null) {
                headerMap = new LinkedHashMap<>(16);
            }
            headerMap.putAll(headerMapNew);
        }
        return this;
    }


    /**
     * 初始化get方法
     *
     * @return
     */
    public OkHttpUtils get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttpUtils post(Utf8MediaType utf8MediaType, String data) {
        RequestBody requestBody;
        StringBuilder content = new StringBuilder("");
        if (StringUtils.isBlank(data)) {
            if (paramMap != null) {
                Iterator<Map.Entry<String, Object>> iterator = paramMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    content.append(entry.getKey()).append("=").append(entry.getValue());
                    if (iterator.hasNext()) {
                        content.append("&");
                    }
                }
            }
        } else {
            content.append(data);
        }
        requestBody = RequestBody.create(utf8MediaType.getMediaType(), content.toString());
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttpUtils postFormData() {
        RequestBody requestBody;
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        if (paramMap != null) {
            paramMap.forEach((key, value) -> {
                if (value instanceof byte[]) {
                    multipartBody.addFormDataPart(key, key, RequestBody.create(Utf8MediaType.MEDIA_TYPE_FILE.getMediaType(), (byte[]) value));
                } else if (value instanceof FormFileData) {
                    FormFileData formFileData = (FormFileData) value;
                    multipartBody.addFormDataPart(key, formFileData.getFileName(), RequestBody.create(Utf8MediaType.MEDIA_TYPE_FILE.getMediaType(), formFileData.getFileContent()));
                } else {
                    multipartBody.addFormDataPart(key, String.valueOf(value));
                }
            });
        }
        requestBody = multipartBody.build();
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttpUtils postFormUrl() {
        RequestBody requestBody;
        FormBody.Builder formBody = new FormBody.Builder();
        if (paramMap != null) {
            paramMap.forEach((key, value) -> formBody.add(key, String.valueOf(value)));
        }
        requestBody = formBody.build();
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    /**
     * post 请求json
     *
     * @return
     */
    public OkHttpUtils postJson() {
        RequestBody requestBody;
        String json = "";
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_JSON.getMediaType(), json);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    /**
     * post 请求json
     *
     * @param json json字符串
     * @return
     */
    public OkHttpUtils postJson(String json) {
        RequestBody requestBody;
        if (StringUtils.isBlank(json)) json = "{}";
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_JSON.getMediaType(), json);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttpUtils postBody(String data) {
        RequestBody requestBody;
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_BODY.getMediaType(), data);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttpUtils postFile(byte[] content) {
        RequestBody requestBody;
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_FILE.getMediaType(), content);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        Response response = null;
        try {
            response = okHttpClient.newCall(request.build()).execute();
            assert response != null;
            assert response.body() != null;
            String data = response.body().string();
            LOGGER.debug("okhttp return ->request:{},response:{},dataStr:{}", request, response, data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("okhttp return ->request:{},response:{}", request, response);
            return "";
        }
    }


    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body().string());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    private static X509TrustManager buildX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // 不验证
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }
}
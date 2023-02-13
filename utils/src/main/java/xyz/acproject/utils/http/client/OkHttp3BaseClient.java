package xyz.acproject.utils.http.client;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.acproject.utils.http.OkHttpUtils;
import xyz.acproject.utils.http.data.FormFileData;
import xyz.acproject.utils.http.type.Utf8MediaType;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName OkHttp3BaseClient
 * @Description TODO
 * @date 2022/7/7 11:32
 * @Copyright:2022
 */
@Getter
public abstract class OkHttp3BaseClient {
    private static final Logger LOGGER = LogManager.getLogger(OkHttp3BaseClient.class);
    private Map<String, Object> headerMap;
    private Map<String, Object> paramMap;
    private String url;
    private Request.Builder request;
    private boolean log = false;

    private boolean ssl = false;

    public static final int READ_TIMEOUT = 15;
    public static final int CONNECT_TIMEOUT = 15;
    public static final int WRITE_TIMEOUT = 15;


    public abstract OkHttpClient client();

    public abstract Semaphore semaphore();

    public abstract ConnectionPool pool();

    public ConnectionPool defaultPool() {
        return new ConnectionPool(50, 5, TimeUnit.MINUTES);
    }


    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttp3BaseClient url(String url) {
        this.url = url;
        return this;
    }

    public OkHttp3BaseClient log() {
        this.log = true;
        return this;
    }

    public OkHttp3BaseClient log(boolean enable) {
        this.log = enable;
        return this;
    }

    public OkHttp3BaseClient ssl() {
        this.ssl = true;
        return this;
    }

    public OkHttp3BaseClient ssl(boolean enable) {
        this.ssl = enable;
        return this;
    }


    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttp3BaseClient addParam(String key, Object value) {
        if (value != null) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(16);
            }
            paramMap.put(key, value);
        }
        return this;
    }


    public OkHttp3BaseClient addParam(boolean condition, String key, Object value) {
        if(condition) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(16);
            }
            paramMap.put(key, value);
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
    public OkHttp3BaseClient addHeader(String key, Object value) {
        if (value != null) {
            if (headerMap == null) {
                headerMap = new LinkedHashMap<>(16);
            }
            headerMap.put(key, value);
        }
        return this;
    }

    public OkHttp3BaseClient addHeader(boolean condition, String key, Object value) {
        if (condition) {
            if (headerMap == null) {
                headerMap = new LinkedHashMap<>(16);
            }
            headerMap.put(key, value);
        }
        return this;
    }


    /**
     * 初始化get方法
     *
     * @return
     */
    public OkHttp3BaseClient get() {
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


    public OkHttp3BaseClient post(Utf8MediaType utf8MediaType, String data) {
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


    public OkHttp3BaseClient postFormData() {
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


    public OkHttp3BaseClient postFormUrl() {
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
    public OkHttp3BaseClient postJson() {
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
    public OkHttp3BaseClient postJson(String json) {
        RequestBody requestBody;
        if (StringUtils.isBlank(json)) json = "{}";
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_JSON.getMediaType(), json);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttp3BaseClient postBody(String data) {
        RequestBody requestBody;
        requestBody = RequestBody.create(Utf8MediaType.MEDIA_TYPE_BODY.getMediaType(), data);
        request = new Request.Builder().post(requestBody).url(url);
        if (log) {
            LOGGER.debug("okhttp -> request:{}", request);
        }
        return this;
    }


    public OkHttp3BaseClient postFile(byte[] content) {
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
        String data = "";
        try {
            response = client().newCall(request.build()).execute();
            assert response != null;
            assert response.body() != null;
            data = response.body().string();
            LOGGER.debug("okhttp return ->request:{},response:{},dataStr:{}", request, response, data);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("okhttp return ->request:{},response:{}", request, response);
        }
        return data;
    }


    public String sync(IRetry retry) {
        setHeader(request);
        Response response = null;
        String data = "";
        try {
            response = client().newCall(request.build()).execute();
            assert response != null;
            assert response.body() != null;
            data = response.body().string();
            LOGGER.debug("okhttp return ->request:{},response:{},dataStr:{}", request, response, data);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("okhttp return ->request:{},response:{}", request, response);
        }
        return retry.retry(this, data);
    }


    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        client().newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                try {
                    semaphore().release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            semaphore().acquire();
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
        client().newCall(request.build()).enqueue(new Callback() {
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
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);
    }

    public interface IRetry {
        String retry(OkHttp3BaseClient client, String data);
    }
}

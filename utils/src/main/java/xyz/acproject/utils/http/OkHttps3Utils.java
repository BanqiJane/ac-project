package xyz.acproject.utils.http;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName OkHttps3Utils
 * @Description TODO
 * @date 2021/6/18 17:40
 * @Copyright:2021
 */
public class OkHttps3Utils {

    private static final Logger LOGGER = LogManager.getLogger(OkHttps3Utils.class);
    private volatile static OkHttps3Utils okHttps3Utils;

    /**
     * HTTP实例
     * <p>
     * 确保本条指令不会因编译器的优化而省略，且要求每次直接读值
     */
    private OkHttpClient okHttpClient;

    /** ssl socket工厂 */
    private static SSLSocketFactory sslSocketFactory = null;

    private static X509TrustManager trustManager = null;

    /** ssl socket工厂（需要校验CA） */
    private static SSLSocketFactory sslSocketFactoryVerifyCa = null;

    private static X509TrustManager trustManagerVerifyCa = null;

    // MEDIA_TYPE <==> Content-Type
    private static final int READ_TIMEOUT = 15;
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    // MEDIA_TYPE_TEXT
    // post请求不是application/x-www-form-urlencoded的，全部直接返回，不作处理，即不会解析表单数据来放到request
    // parameter map中。所以通过request.getParameter(name)是获取不到的。只能使用最原始的方式，读取输入流来获取。
    private static final MediaType MEDIA_TYPE_TEXT = MediaType
            .parse("application/x-www-form-urlencoded; charset=utf-8");

    private static final MediaType MEDIA_TYPE_BODY  = MediaType
            .parse("text/plain; charset=utf-8");

    private static final MediaType MEDIA_TYPE_FILE = MediaType
            .parse("application/octet-stream");

    private OkHttps3Utils(boolean needVerifyCa, InputStream caInputStream, String cAalias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.connectionPool(pool());
        // 先调用helper方法，初始化
        httpsHelper(needVerifyCa, caInputStream, cAalias);
        // 需要校验证书
        if (needVerifyCa) {
            builder.sslSocketFactory(sslSocketFactoryVerifyCa, trustManagerVerifyCa);
            /*
             * 情况一: 证书中预设有 hostname
             *    证书中会预设一些hostname(即: 预设一些ip、域名)， 只有当请求url中的ip/域名，是包含在预设的那些hostname中的时，
             * 才会认证通过，否者会报【The certificate hostname does not match】之类的问题。如果证书中预设有一些hostname
             * 的话，我们这里就不需要在再设置hostname校验规则了，会走默认的hostname校验。
             *
             *
             * 情况二: 证书中没有预设 hostname
             *    如果连证书中都没有预设hostname的话，即:服务端认为不需要验证hostname, 这时，我们作为客户端，需要重写hostname校验规则，
             * 不让其走默认的校验规则(因为默认会对hostname进行校验)。即:不论是什么，直接返回true即可，表示所有的hostname都成功，
             * 在此处代码里，只需要builder.hostnameVerifier((String hostname, SSLSession session) -> true)即可达到设
             * 置不作hostname校验的效果。
             *    当然，如果你非要校验，你也可以自己在客户端通过builder.hostnameVerifier(HostnameVerifier hostnameVerifier)
             * 设置hostname校验规则。
             */
        }else {
            // 不需要校验证书
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            // 不校验 url中的hostname(直接返回true，表示不校验hostname)
            // 注:hostname 指 ip/域名
            builder.hostnameVerifier((String hostname, SSLSession session) -> true);
        }
        okHttpClient = builder.build();
    }
    /**
     * 连接池 复用http
     */
    private static ConnectionPool pool() {
        return new ConnectionPool(100, 5, TimeUnit.MINUTES);
    }

    public static OkHttps3Utils getHttps3Utils(boolean needVerifyCa, InputStream caInputStream, String cAalias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {

        if(okHttps3Utils==null) {
            synchronized (OkHttp3Utils.class) {
                if(okHttps3Utils==null) {
                    okHttps3Utils = new OkHttps3Utils(needVerifyCa, caInputStream, cAalias);
                }
            }
        }
        return okHttps3Utils;
    }
    public Response httpGet(String url, Map<String, String> headers, Map<String, Object> datas) throws Exception{
        Headers hearderHeaders = null;
        StringBuilder stringBuilder= null;
        Request request=null;
        if (datas != null && datas.size() > 0) {
            stringBuilder = new StringBuilder(100);
            if(url.indexOf("?")<0&&url.indexOf("=")<0) {
                stringBuilder.append("?");
            }else{
                if(!url.endsWith("&")){
                    stringBuilder.append("&");
                }
            }
            for (Map.Entry<String, Object> entry : datas.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        }
        if(stringBuilder!=null) {
            url = stringBuilder.insert(0, url).toString();
        }
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).get().build();
        }else {
            request = new Request.Builder().url(url).get().build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"GET",headers!=null?headers.toString():null,datas!=null?datas.toString():null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttp return -> response:{}",response);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		}finally {
//			if(headers!=null)headers.clear();
//			if(datas!=null)datas.clear();
//		}
        return response;
    }
    public Response  httpPostJson(String url,Map<String, String> headers,String json) throws Exception{
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request=null;
        Headers hearderHeaders = null;
        Response response = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,json);
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//		}
        return response;
    }
    public Response httpPostForm(String url, Map<String, String> headers, Map<String, Object> params) throws Exception {
        Request request=null;
        StringBuilder content = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            content.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                content.append("&");
            }
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, content.toString());
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,params!=null?params.toString():null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
        return response;
    }

    public Response httpPostFileForm(String url, Map<String, String> headers, byte[] fileBytes) throws Exception {
        Request request=null;
        RequestBody body  = MultipartBody.create(MEDIA_TYPE_FILE, fileBytes);
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},size:{}",url,"POST-FILE",headers!=null?headers.toString():null,fileBytes.length);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//		}
        return response;
    }


    public Response httpPostBody(String url,Map<String, String> headers,String data){
        Request request=null;
        RequestBody body = RequestBody.create(MEDIA_TYPE_BODY, data);
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,data!=null?data:null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttp return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
        return response;
    }


    //自定义 client

    public Response httpGet(String url, Map<String, String> headers, Map<String, Object> datas,OkHttpClient okHttpClient) throws Exception{
        Headers hearderHeaders = null;
        StringBuilder stringBuilder= null;
        Request request=null;
        if (datas != null && datas.size() > 0) {
            stringBuilder = new StringBuilder(100);
            if(url.indexOf("?")<0&&url.indexOf("=")<0) {
                stringBuilder.append("?");
            }else{
                if(!url.endsWith("&")){
                    stringBuilder.append("&");
                }
            }
            for (Map.Entry<String, Object> entry : datas.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        }
        if(stringBuilder!=null) {
            url = stringBuilder.insert(0, url).toString();
        }
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).get().build();
        }else {
            request = new Request.Builder().url(url).get().build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"GET",headers!=null?headers.toString():null,datas!=null?datas.toString():null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		}finally {
//			if(headers!=null)headers.clear();
//			if(datas!=null)datas.clear();
//		}
        return response;
    }
    public Response  httpPostJson(String url,Map<String, String> headers,String json,OkHttpClient okHttpClient) throws Exception{
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request=null;
        Headers hearderHeaders = null;
        Response response = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,json);
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//		}
        return response;
    }
    public Response httpPostForm(String url, Map<String, String> headers, Map<String, Object> params,OkHttpClient okHttpClient) throws Exception {
        Request request=null;
        StringBuilder content = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            content.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                content.append("&");
            }
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, content.toString());
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,params!=null?params.toString():null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
        return response;
    }

    public Response httpPostFileForm(String url, Map<String, String> headers, byte[] fileBytes, OkHttpClient okHttpClient) throws Exception {
        Request request=null;
        RequestBody body  = MultipartBody.create(MEDIA_TYPE_FILE, fileBytes);
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttps -> url:{},method:{},headers:{},size:{}",url,"POST-FILE",headers!=null?headers.toString():null,fileBytes.length);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttps return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//		}
        return response;
    }

    public Response httpPostBody(String url,Map<String, String> headers,String data, OkHttpClient okHttpClient){
        Request request=null;
        RequestBody body = RequestBody.create(MEDIA_TYPE_BODY, data);
        Headers hearderHeaders = null;
        if (headers != null && headers.size() > 0) {
            hearderHeaders = Headers.of(headers);
            request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
        }else {
            request = new Request.Builder().url(url).post(body).build();
        }
        LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,data!=null?data:null);
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            LOGGER.debug("okhttp return -> response:{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
        return response;
    }



    /* ---------------------------------------------------证书相关  证书相关  证书相关---------------------------------------------------------------  */
    /**
     * HTTPS辅助方法, 为HTTPS请求 创建SSLSocketFactory实例、TrustManager实例
     *
     * @param needVerifyCa
     *         是否需要检验CA证书(即:是否需要检验服务器的身份)
     * @param caInputStream
     *         CA证书。(若不需要检验证书，那么此处传null即可)
     * @param cAalias
     *         别名。(若不需要检验证书，那么此处传null即可)
     *         注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @throws NoSuchAlgorithmException
     *         异常信息
     * @throws CertificateException
     *         异常信息
     * @throws KeyStoreException
     *         异常信息
     * @throws IOException
     *         异常信息
     * @throws KeyManagementException
     *         异常信息
     * @date 2019/6/11 19:52
     */
    private static void httpsHelper(boolean needVerifyCa, InputStream caInputStream, String cAalias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, KeyManagementException {
        // https请求，需要校验证书
        if (needVerifyCa) {
            KeyStore keyStore = getKeyStore(caInputStream, cAalias);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManagerVerifyCa = (X509TrustManager) trustManagers[0];
            // 这里传TLS或SSL其实都可以的
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManagerVerifyCa}, new SecureRandom());
            sslSocketFactoryVerifyCa = sslContext.getSocketFactory();
            return;
        }
        // https请求，不作证书校验
        trustManager = new X509TrustManager() {
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
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        sslSocketFactory = sslContext.getSocketFactory();
    }

    /**
     * 获取(密钥及证书)仓库
     * 注:该仓库用于存放 密钥以及证书
     *
     * @param caInputStream
     *         CA证书(此证书应由要访问的服务端提供)
     * @param cAalias
     *         别名
     *         注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @return 密钥、证书 仓库
     * @throws KeyStoreException 异常信息
     * @throws CertificateException 异常信息
     * @throws IOException 异常信息
     * @throws NoSuchAlgorithmException 异常信息
     * @date 2019/6/11 18:48
     */
    private static KeyStore getKeyStore(InputStream caInputStream, String cAalias)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        // 证书工厂
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 秘钥仓库
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        keyStore.setCertificateEntry(cAalias, certificateFactory.generateCertificate(caInputStream));
        return keyStore;
    }



}

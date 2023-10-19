package xyz.acproject.utils.http.client;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import xyz.acproject.utils.http.OkHttpUtils;
import xyz.acproject.utils.http.config.OkHttpConfig;
import xyz.acproject.utils.security.CertUtils;
import xyz.acproject.utils.security.SslUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName OkHttps3Client
 * @Description https的基础样例写法
 * @date 2022/7/7 14:03
 * @Copyright:2022
 */
public class OkHttps3Client extends OkHttp3BaseClient {


    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;

    /**
     * ssl socket工厂（需要校验CA）
     */
    private static SSLSocketFactory sslSocketFactoryVerifyCa = null;

    private static X509TrustManager trustManagerVerifyCa = null;


    /**
     * 初始化okHttpClient
     */
    private OkHttps3Client() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectionPool(pool());
                    if (!super.isSsl()) {
                        builder.sslSocketFactory(SslUtils.createSSLSocketFactory(SslUtils.buildTrustManagers()), SslUtils.buildX509TrustManager());
                        builder.hostnameVerifier((hostName, session) -> true);
                    }
//                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                }
            }
        }
    }
    private OkHttps3Client(byte[] caBytes, String cAalias) {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectionPool(pool());
                    if (!super.isSsl()) {
                        builder.sslSocketFactory(SslUtils.createSSLSocketFactory(SslUtils.buildTrustManagers()), SslUtils.buildX509TrustManager());
                        builder.hostnameVerifier((hostName, session) -> true);
                    }else{
                        try {
                            httpsInit(caBytes, cAalias);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    builder.retryOnConnectionFailure(true)
                    };
                    okHttpClient = builder.build();
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                }
            }
        }
    }

    private OkHttps3Client(OkHttpConfig okHttpConfig) {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(okHttpConfig.READ_TIMEOUT(), TimeUnit.SECONDS);
                    builder.connectTimeout(okHttpConfig.CONNECT_TIMEOUT(), TimeUnit.SECONDS);
                    builder.writeTimeout(okHttpConfig.WRITE_TIMEOUT(), TimeUnit.SECONDS);
                    builder.connectionPool(okHttpConfig.pool());
                    log(okHttpConfig.LOGGER());
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
    public static OkHttps3Client builder() {
        return new OkHttps3Client();
    }


    public static OkHttps3Client builder(OkHttpConfig okHttpConfig) {
        return new OkHttps3Client(okHttpConfig);
    }


    @Override
    public OkHttpClient client() {
        return okHttpClient;
    }

    @Override
    public Semaphore semaphore() {
        return getSemaphoreInstance();
    }

    @Override
    public ConnectionPool pool() {
        return defaultPool();
    }


    private static void httpsInit(byte[] caBytes, String cAalias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, KeyManagementException {
        // https请求，需要校验证书
        KeyStore keyStore = CertUtils.getKeyStore(caBytes, cAalias);
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
    }

}

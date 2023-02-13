package xyz.acproject.utils.http.client;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import xyz.acproject.utils.http.OkHttpUtils;
import xyz.acproject.utils.http.config.OkHttpConfig;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName OkHttp3Client
 * @Description TODO
 * @date 2022/7/7 11:35
 * @Copyright:2022
 */
public class OkHttp3Client extends OkHttp3BaseClient{

    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;


    /**
     * 初始化okHttpClient
     */
    private OkHttp3Client() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
                    builder.connectionPool(pool());
//                    builder.sslSocketFactory(createSSLSocketFactory(buildTrustManagers()), buildX509TrustManager());
//                    builder.hostnameVerifier((hostName, session) -> true);
//                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                }
            }
        }
    }

    private OkHttp3Client(OkHttpConfig okHttpConfig) {
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
    public static OkHttp3Client builder() {
        return new OkHttp3Client();
    }


    public static OkHttp3Client builder(OkHttpConfig okHttpConfig) {
        return new OkHttp3Client(okHttpConfig);
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
}

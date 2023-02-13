package xyz.acproject.utils.http.config;

import lombok.Data;
import okhttp3.ConnectionPool;

/**
 * @author Jane
 * @ClassName OkHttpConfig
 * @Description TODO
 * @date 2022/7/3 22:49
 * @Copyright:2022
 */
@Data
public abstract class OkHttpConfig {
    public abstract int READ_TIMEOUT();
    public abstract int CONNECT_TIMEOUT();
    public abstract int WRITE_TIMEOUT();
    public abstract boolean LOGGER();
    public abstract ConnectionPool pool();
}

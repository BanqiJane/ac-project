package xyz.acproject.security_flux_demo.dtos;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Admin
 * @ClassName DanMu
 * @Description TODO
 * @date 2023/1/13 10:36
 * @Copyright:2023
 */
@Data
public class DanMu implements Delayed {
    private String uuid;

    private String msg;

    private long expiredTime =0;
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert(this.getExpiredTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        return Long.compare(this.getExpiredTime(), ((DanMu) o).getExpiredTime());
    }
}

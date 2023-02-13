package xyz.acproject.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Jane
 * @ClassName TimeOutUtils
 * @Description TODO
 * @date 2021/8/11 10:03
 * @Copyright:2021
 * @source refer to https://www.cnblogs.com/luliang888/p/14440118.html
 */
public class TimeOutUtils {
    /**
     * Singleton delay scheduler, used only for starting and * cancelling tasks.
     */
    static final class Delayer {
        static ScheduledFuture<?> delay(Runnable command, long delay,
                                        TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureDelayScheduler");
                return t;
            }
        }

        static final ScheduledThreadPoolExecutor delayer;

        // 注意，这里使用一个线程就可以搞定 因为这个线程并不真的执行请求 而是仅仅抛出一个异常
        static {
            (delayer = new ScheduledThreadPoolExecutor(
                    1, new TimeOutUtils.Delayer.DaemonThreadFactory())).
                    setRemoveOnCancelPolicy(true);
        }
    }

    public static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
        final CompletableFuture<T> promise  = new CompletableFuture<T>();
        final TimeoutException timeoutException  = new TimeoutException("Timeout after："+timeout);
        // timeout 时间后 抛出TimeoutException 类似于sentinel / watcher
        TimeOutUtils.Delayer.delayer.schedule(() -> promise.completeExceptionally(timeoutException), timeout, unit);
        return promise ;
    }

    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
     *
     * @param t
     * @param future
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> completeOnTimeout(T t, CompletableFuture<T> future, long timeout, TimeUnit unit) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return future.applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> t);
    }

    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
     *
     * @param t
     * @param supplier
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> completeOnTimeout(T t, Supplier<T> supplier, long timeout, TimeUnit unit) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return CompletableFuture.supplyAsync(supplier).applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> t);
    }


    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API，不设置默认值，超时后抛出异常
     *
     * @param t
     * @param future
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> orTimeout(T t, CompletableFuture<T> future, long timeout, TimeUnit unit) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return future.applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> t);
    }


    //使用示例
    public static void main(String[] args) {
        String s = null;
        while(StringUtils.isBlank(s)||"异常".equals(s)) {
            CompletableFuture<String> futureStr = CompletableFuture.supplyAsync(() -> {
                try {
                    long sleepMills = RandomUtils.nextInt(1000, 10000);
                    System.out.println("睡眠:"+sleepMills);
                    Thread.sleep(sleepMills);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "正常执行";
            });
            try {
                CompletableFuture<String> future = completeOnTimeout("异常",futureStr,5,TimeUnit.SECONDS);
                s = future.get();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

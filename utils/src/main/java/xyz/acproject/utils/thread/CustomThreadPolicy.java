package xyz.acproject.utils.thread;

import xyz.acproject.utils.random.RandomUtils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jane
 * @ClassName CreateThreadPolicy
 * @Description TODO
 * @date 2021/5/13 14:07
 * @Copyright:2021
 */
public class CustomThreadPolicy {

    public static class CreateThreadsPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code CallerRunsPolicy}.
         */
        public CreateThreadsPolicy() { }

        /**
         * Executes task r in the caller's thread, unless the executor
         * has been shut down, in which case the task is discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
             //不受线程池控制了
//            if (!e.isShutdown()) {
                new Thread(r, "thread-"+RandomUtils.getUUID()).start();
//            }
        }
    }
}

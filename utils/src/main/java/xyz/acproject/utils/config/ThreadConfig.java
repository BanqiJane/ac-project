package xyz.acproject.utils.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jane
 * @ClassName ThreadConfig
 * @Description 多线程任务 满了会阻塞主线程   分页执行建议 保持4的倍数  即总数/pageSize 如前后误差 多建议为后者  即30/8
 * @date 2021/4/6 11:55
 * @Copyright:2021
 */
public class ThreadConfig {

    /**
     * corePoolSize 池中所保存的线程数，包括空闲线程。
     */
    private static final int corePoolSize = 40;
    /**
     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。
     */
    private static final int maximumPoolSize = 40;
    /**
     * keepAliveTime -当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间
     */
    private static final int keepAliveTime = 60;

    /**
     * 执行前用于保持任务的队列（缓冲队列）
     */
    private static final int capacity = 300;

    /**
     * 线程池对象
     */
    private volatile static ThreadPoolTaskExecutor threadPoolTaskExecutor;


    //构造方法私有化
    private ThreadConfig() {
    }

    public static Executor getThreadPoolExecutor() {

        if (null == threadPoolTaskExecutor) {
            synchronized (ThreadConfig.class) {
                if(threadPoolTaskExecutor==null) {
                    threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
                    //核心线程数:线程池刚创建的时候 就会初始化线程
                    threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
                    //非核心线程： 核心线程不够用的时候
                    threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
                    //缓冲队列的个数 :队列作为一个缓冲的工具，
                    //当没有足够的线程去处理任务时，可以将任务放进队列中，以队列先进先出的特性来执行工作任务
                    threadPoolTaskExecutor.setQueueCapacity(capacity);
                    //非核心线程数 还回去的时间  如果空闲超过10秒就被回收
                    threadPoolTaskExecutor.setKeepAliveSeconds(3);
                    //设置线程的前缀名称
                    threadPoolTaskExecutor.setThreadNamePrefix("fangulupu_");
                    //用来设置线程池关闭的时候等待所有任务都完成(可以设置时间)
                    threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
                    //设置上面的时间
                    threadPoolTaskExecutor.setAwaitTerminationSeconds(10);
                    //拒绝策略:线程池都忙不过来的时候，可以适当选择拒绝
                    /**
                     *  ThreadPoolExecutor.AbortPolicy();//默认，队列满了丢任务抛出异常
                     * ThreadPoolExecutor.DiscardPolicy();//队列满了丢任务不异常
                     * ThreadPoolExecutor.DiscardOldestPolicy();//将最早进入队列的任务删，之后再尝试加入队列
                     * ThreadPoolExecutor.CallerRunsPolicy();//如果添加到线程池失败，那么主线程会自己去执行该任务
                     */
                    threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

                    //初始化线程池
                    threadPoolTaskExecutor.initialize();
                }
            }
        }
        return threadPoolTaskExecutor;
    }
//
//    public static ThreadPoolExecutor getThreadPoolExecutor() {
//        if (null == threadPoolExecutor) {
//            synchronized (ThreadConfig.class) {
//                if(threadPoolExecutor==null) {
//                    LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(capacity);
//                    // new RejectedExecutionHandler() {
//                    //                        @Override
//                    //                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//                    //                            if (!e.isShutdown()) {
//                    //                                while (e.getQueue().remainingCapacity() == 0) ;
//                    //                                e.execute(r);
//                    //                            }
//                    //                        }
//                    //                    }
//                    threadPoolExecutor
//                            =
//                            new ThreadPoolExecutor(corePoolSize, maximumPoolSize, time, timeUnit,linkedBlockingQueue,new ThreadPoolExecutor.CallerRunsPolicy());
//                }
//            }
//        }
//        return threadPoolExecutor;
//    }

}

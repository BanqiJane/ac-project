package xyz.acproject.utils.task;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jane
 * @ClassName ShedulingTaskCenter
 * @Description TODO
 * @date 2021/4/23 14:46
 * @Copyright:2021
 */
public class SchedulingTaskCenter implements DisposableBean {

    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(this.getPoolSize());

    private int poolSize = 3;

    private TaskScheduler taskScheduler;

    public int getPoolSize() {
        if(this.poolSize<=0){
            return 1;
        }
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }
    /**
     * 添加任务
     *
     * @param task
     * @param expression
     */
    public void addTask(Runnable task,String expression) {
        addTask(new CronTask(task, expression));
    }

    public void addTask(CronTask cronTask) {
        if(cronTask!=null) {
            Runnable task = cronTask.getRunnable();
            if(!this.scheduledTasks.containsKey(task)) {
                this.scheduledTasks.put(task,scheduledTask(cronTask));
            }
        }
    }

    public boolean hasTask(Runnable task){
        return this.scheduledTasks.containsKey(task);
    }

    /**
     * 移除指定任务
     * @param task
     */
    public void removeTask(Runnable task) {
        ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
        if(scheduledTask!=null) {
            scheduledTask.cancel();
        }
    }

    public Map<Runnable, ScheduledTask> getScheduledTasks(){
        return this.scheduledTasks;
    }


    public ScheduledTask scheduledTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    public int getTaskSize() {
        return this.scheduledTasks.size();
    }
    /**
     * 清除所有任务
     */
    @Override
    public void destroy() throws Exception {
        // TODO 自动生成的方法存根
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }
        this.scheduledTasks.clear();
    }
}

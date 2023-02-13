package xyz.acproject.utils.task;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ReflectionUtils;
import xyz.acproject.utils.SpringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jane
 * @ClassName SchedulingRunnable
 * @Description TODO
 * @date 2021/4/23 12:06
 * @Copyright:2021
 */
public class SchedulingRunnable implements Runnable{
    private static final Logger LOGGER  = LogManager.getLogger(SchedulingRunnable.class);

    private String beanName;

    private String methodName;

    private Object[] params;

    public SchedulingRunnable() {
    }

    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName, methodName, new  Object[]{});
    }

    public SchedulingRunnable(String beanName, String methodName, Object...params ) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    public SchedulingRunnable beanName(String beanName){
        this.beanName = beanName;
        return this;
    }

    public SchedulingRunnable methodName(String methodName){
        this.methodName = methodName;
        return this;
    }

    public SchedulingRunnable addParam(Object... datas){
        if (null == datas || datas.length <= 0) return this;
        if (null != this.params)
            this.params = ArrayUtils.addAll(this.params,datas);
        else
            this.params = new Object[]{};
            this.params = ArrayUtils.addAll(this.params,datas);
        return this;
    }


    @Override
    public void run() {
        synchronized (methodName) {
            LOGGER.info("定时任务开始执行 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
            long startTime = System.currentTimeMillis();

            try {
                Object target = SpringUtils.getBean(beanName);
                Method method = null;
                if (null != params && params.length > 0) {
                    Class<?>[] paramCls = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        paramCls[i] = params[i].getClass();
                    }
                    method = target.getClass().getDeclaredMethod(methodName, paramCls);
                } else {
                    method = target.getClass().getDeclaredMethod(methodName);
                }

                ReflectionUtils.makeAccessible(method);
                if (null != params && params.length > 0) {
                    method.invoke(target, params);
                } else {
                    method.invoke(target);
                }
            } catch (Exception ex) {
                LOGGER.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), ex);
            }
            long times = System.currentTimeMillis() - startTime;
            LOGGER.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (this.params == null) {
            return this.beanName.equals(that.beanName) &&
                    this.methodName.equals(that.methodName) &&
                    that.params == null;
        }

        return this.beanName.equals(that.beanName) &&
                this.methodName.equals(that.methodName) &&
                Arrays.equals(this.params,that.params);
    }

    @Override
    public int hashCode() {
//        if (params == null) {
//            return Objects.hash(beanName, methodName);
//        }
//
//        return Objects.hash(beanName, methodName, params);
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBeanName() == null) ? 0 : getBeanName().hashCode());
        result = prime * result + ((getMethodName() == null) ? 0 : getMethodName().hashCode());
        result = prime * result + (((this.params == null) ? 0 :Arrays.hashCode(this.params)));
        return result;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

package xyz.acproject.utils.progress;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jane
 * @ClassName ProgressStatus
 * @Description TODO
 * @date 2021/5/26 11:06
 * @Copyright:2021
 */
public class ProgressStatus implements Serializable {
    private ProgressStatus() {
    }
    private static Map<String,Long> taskProgressTotal = new ConcurrentHashMap<>();
    private static Map<String,Long> taskProgress = new ConcurrentHashMap<>();

    public static Map<String, Long> getTaskProgress() {
        return ProgressStatus.taskProgress;
    }
    public static Long getTaskProgressCount(String id){
        return ProgressStatus.taskProgress.get(id);
    }

    public static String getTaskProgressPercentage(String id){
        Long total = ProgressStatus.taskProgressTotal.get(id);
        Long now = ProgressStatus.taskProgress.get(id);
        if(total!=null&&now!=null&&total!=0){
            return new BigDecimal(100).subtract(new BigDecimal(now).divide(new BigDecimal(total),6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"%";
        }
        return "0%";
    }
    public static Map<String,Long> put(String id, Long num){
        ProgressStatus.taskProgress.put(id,num);
        return ProgressStatus.taskProgress;
    }
    public static Map<String,Long> putTotal(String id, Long num){
        ProgressStatus.taskProgressTotal.put(id,num);
        return ProgressStatus.taskProgressTotal;
    }
    public static Map<String,Long> decr(String id,Integer count){
        if(ProgressStatus.taskProgress.get(id)!=null) {
            ProgressStatus.taskProgress.put(id, ProgressStatus.taskProgress.get(id) - count);
        }
        return ProgressStatus.taskProgress;
    }
    public static void remove(String id){
        ProgressStatus.taskProgress.remove(id);
        ProgressStatus.taskProgressTotal.remove(id);
    }
}

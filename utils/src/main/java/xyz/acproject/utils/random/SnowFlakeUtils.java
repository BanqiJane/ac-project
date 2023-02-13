package xyz.acproject.utils.random;

import xyz.acproject.utils.entity.SnowFlake;

/**
 * @author Jane
 * @ClassName SnowFlakeUtils
 * @Description TODO
 * @date 2021/3/23 22:47
 * @Copyright:2021
 * @From https://www.jianshu.com/go-wild?ac=2&url=https%3A%2F%2Fsegmentfault.com%2Fa%2F1190000011282426
 */
public class SnowFlakeUtils {
    private volatile static SnowFlake snowFlake;

    public static SnowFlake getSnowFlake(long datacenterId, long machineId) {
        if(snowFlake==null){
            synchronized (SnowFlake.class){
                if(snowFlake==null){
                    snowFlake = new SnowFlake(datacenterId, machineId);
                }
            }
        }
        return snowFlake;
    }
}

package xyz.acproject.utils.random;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * @author Jane
 * @ClassName RandomUtils
 * @Description TODO
 * @date 2021/1/25 15:42
 * @Copyright:2021
 */
public final class RandomUtils {

    public static final String code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String shuzi_code = "0123456789";

    public static final String snowFlakeId(long datacenterId, long machineId){
        return SnowFlakeUtils.getSnowFlake(datacenterId, machineId).nextId()+"";
    }

    public final static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    public static String getRandomCode(int num,String code){
        if (StringUtils.isBlank(code)) {
            code = RandomUtils.code;
        }
        String randomCode = "";
//        if(num>code.length()){
//            return randomCode;
//        }
        char[] codes =code.toCharArray();
        for(int i =0;i<num;i++){
            randomCode += codes[(int)(Math.random()*(codes.length-1))];
        }
        return randomCode;
    }
    public static String get3Random(){
        int flag = new Random().nextInt(999);
        if (flag < 100)
        {
            flag += 100;
        }
        return flag+"";
    }




    public static String get6Random(){
        int flag = new Random().nextInt(999999);
        if (flag < 100000)
        {
            flag += 100000;
        }
        return flag+"";
    }


    public static String get8Random(){
        int flag = new Random().nextInt(99999999);
        if (flag < 10000000)
        {
            flag += 10000000;
        }
        return flag+"";
    }


    public static BigDecimal getRandomBigDecimalTwo(BigDecimal min, BigDecimal max) {
        double minD = min.doubleValue();
        double maxD = max.doubleValue();
        // 生成随机数
        BigDecimal db = new BigDecimal( org.apache.commons.lang3.RandomUtils.nextDouble(minD,maxD));

        // 返回保留两位小数的随机数。不进行四舍五入
        return db.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

}

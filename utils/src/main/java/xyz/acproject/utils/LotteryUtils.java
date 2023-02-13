package xyz.acproject.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import xyz.acproject.lang.entity.LotteryBean;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.exception.CustomException;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Jane
 * @ClassName LotteryUtis
 * @Description 抽奖工具类
 * @date 2021/3/24 0:12
 * @Copyright:2021
 */
public class LotteryUtils {

    private final static Logger LOGGER = LogManager.getLogger(LotteryUtils.class);

    public static int generate(Map<Integer, BigDecimal> datas, boolean hasNoWin) {
        if (CollectionUtils.isEmpty(datas)) return 0;

        //打乱他
        Map<Integer, BigDecimal> shuffle_datas = new LinkedHashMap<>();
        List<Integer> keys = new ArrayList<>(datas.keySet());
        Collections.shuffle(keys);
        keys.forEach(k->{shuffle_datas.put(k,datas.get(k));});


        List<LotteryBean> lotteryBeans = new ArrayList<>();
        int winId = 0;
        long number = 10000000000l;//一百个亿
        BigDecimal rateTotal = BigDecimal.ZERO;
        //排除不平整概率
        for (Map.Entry<Integer, BigDecimal> entry : shuffle_datas.entrySet()) {
            rateTotal = rateTotal.add(entry.getValue());
        }
//        Collections.shuffle(lotteryBeans);
        long start = 0;
        //根据概率开始构造区间
        for (Map.Entry<Integer, BigDecimal> entry : shuffle_datas.entrySet()) {
            int goodId = entry.getKey();
            BigDecimal rate = entry.getValue().divide(new BigDecimal("100"));
            long end = rate.multiply(new BigDecimal(String.valueOf(number))).longValue();
            LotteryBean lotteryBean = new LotteryBean();
            lotteryBean.setId(goodId);
            lotteryBean.setRate(rate);
            lotteryBean.setStartNum(start);
            lotteryBean.setEndNum(start+end>number?number:start + end);
            start += end;
            lotteryBeans.add(lotteryBean);
        }
        LOGGER.info("抽奖---------------生成的抽奖区间:{}",lotteryBeans);
        number = rateTotal.divide(new BigDecimal("100")).multiply(new BigDecimal(String.valueOf(number))).longValue();
        long win = RandomUtils.nextLong(1,number);
        Optional<LotteryBean> lotteryBeanOptional = lotteryBeans.stream().filter(lb->lb.getStartNum()<win&&lb.getEndNum()>=win).findFirst();
        if(lotteryBeanOptional.isPresent()){
            winId = lotteryBeanOptional.get().getId();
        }else{
            throw new CustomException(HttpCodeEnum.syserror);
        }
//        for(LotteryBean lotteryBean:lotteryBeans){
//            if(lotteryBean.getStartNum()<win&&lotteryBean.getEndNum()>=win){
//                winId = lotteryBean.getId();
//            }
//        }
        LOGGER.info("中奖结果打印->随机数生成:{},命中id:{}",win,winId);
//        if (hasNoWin && start < number) {
//            LotteryBean lotteryBean = new LotteryBean();
//            lotteryBean.setId(0);
//            lotteryBean.setRate(new BigDecimal(String.valueOf(number)).divide(new BigDecimal(String.valueOf(number)).subtract(new BigDecimal(String.valueOf(start)))));
//            lotteryBean.setStartNum(start);
//            lotteryBean.setEndNum(number);
//        }

        return winId;
    }
}

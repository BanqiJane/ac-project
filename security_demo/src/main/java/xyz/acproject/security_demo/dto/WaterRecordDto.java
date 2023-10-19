package xyz.acproject.security_demo.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Admin
 * @ClassName WaterRecordDto
 * @Description TODO
 * @date 2023/7/22 14:15
 * @Copyright:2023
 */
@Data
public class WaterRecordDto {

    /**
     * 工作模式
     */
    private Option option;

    /**
     * 使用数据
     */
    private UseData useData;

    public UseData getUseData() {
        if(useData!=null){
            try {
                if (CollectionUtils.isNotEmpty(useData.getValues())) {
                    if(this.getOperate()==5) {
                        UseData.ValueData valueData = new UseData.ValueData();
                        valueData.setMinutes(useData.getValues().get(0));
                        valueData.setUsed(useData.getValues().get(1));
                        valueData.setBalance(useData.getValues().get(2));
                        useData.setValueData(valueData);
                    }else if(this.getOperate()==19){

                    }
                }else{
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(useData.getValues()==null)  useData.setValueData(new UseData.ValueData());
        }
        return useData;
    }

    /**
     * 操作
     */
    private Integer Operate;

    /**
     * ret代码
     * <p>
     * 00	操作成功
     * 01	正在执行
     * 02	水表设备暂停使用
     * 03	水表正在使用中
     * 04	余量为0
     * 05	UID不匹配
     * 06	数据错误 (命令)
     */
    private Integer RetCode;

    /**
     * 随机数
     */
    private String RandomNumber;

    /**
     * 内容
     */
    private String Content;

    @Data
    public static class Option {

        /**
         * 工作状态  0-禁止  1-允许
         */
        private Integer StatusFlag;

        /**
         * 开启状态1-开启  0-关闭
         */
        private Integer OnOffFlag;

        /**
         * 过载标志
         */
        private Integer OverLoadFlag;

        /**
         * 序列号
         */
        private String SerialNo;

        /**
         * 传入的数值，以逗号分隔
         */
        private String Value;

        /**
         * 用于保存获取到的值
         */
        private List<BigDecimal> Values;
    }

    @Data
    public static class UseData {

        /**
         * 用户编号 1字节
         */
        private String UserCode;

        /**
         * 两个字节 组号+子号
         */
        private String RID;

        /**
         * 余额列表(一般用于硬件批量上传过来的数据)
         */
        private List<BigDecimal> BalanceList;

        /**
         * 总用量列表(一般用于硬件批量上传过来的数据)
         */
        private List<BigDecimal> TotalSumList;

        /**
         * 用于保存返回的其它的值，如阶段用量，所用时间等
         */
        private List<BigDecimal> Values;

        /**
         * 一般用于传入的值，多个以逗号划分
         */
        private String Value;

        /**
         * 用于保存返回的其它的值，如阶段用量，所用时间等的数组解析
         */
        private ValueData valueData;


//        public ValueData getValueData() {
//            try {
//                if (CollectionUtils.isNotEmpty(Values)) {
//                    ValueData valueData = new ValueData();
//                    valueData.setMinutes(Values.get(0));
//                    valueData.setUsed(Values.get(1));
//                    valueData.setBalance(Values.get(2));
//                    return valueData;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return new ValueData();
//        }

        @Data
        public static class ValueData {

            private BigDecimal minutes = BigDecimal.ZERO;

            private BigDecimal used = BigDecimal.ZERO;;

            private BigDecimal balance = BigDecimal.ZERO;;
        }

    }
}

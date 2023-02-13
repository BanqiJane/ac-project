package xyz.acproject.lang.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Jane
 * @ClassName LotteryBean
 * @Description TODO
 * @date 2021/3/24 0:27
 * @Copyright:2021
 */
@Data
public class LotteryBean {
    private int id;
    private BigDecimal rate;
    private long startNum;
    private long endNum;
    private boolean win;
}

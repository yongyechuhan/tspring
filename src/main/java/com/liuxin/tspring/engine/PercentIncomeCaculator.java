package com.liuxin.tspring.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PercentIncomeCaculator extends ExpressIncomeCaculator {

    @Override
    public Double caculateIncome(String countData) throws Exception {
        String value = getValue();
        logger.info("start caculate income, config is 【{}】%", value);
        if (Objects.isNull(getValue())) {
            throw new Exception("cacluate percentIncome: parse incomeConfig fail");
        }
        // 计算百分比值
        BigDecimal percentVal = new BigDecimal(value).
                divide(new BigDecimal(100)).setScale(4, RoundingMode.HALF_UP);
        // 计算收益
        BigDecimal income = new BigDecimal(countData);
        income = income.multiply(percentVal).setScale(0, RoundingMode.HALF_UP);
        logger.info("caculate income end, income:【{}】", income);
        return income.doubleValue();
    }
}

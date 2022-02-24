package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PercentIncomeCaculator extends ExpressIncomeCaculator {

    public PercentIncomeCaculator() {}

    public PercentIncomeCaculator(String incomeConfig) {
        super(incomeConfig);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PercentIncomeConfig extends IncomeConfig {
        /**
         * 收益值
         */
        private String value;
    }

    @Override
    public String caculateIncome(String countData) throws Exception {
        PercentIncomeConfig config = JSON.parseObject(incomeConfig, PercentIncomeConfig.class);
        logger.debug("start caculate income, config is 【{}】", incomeConfig);
        if (Objects.isNull(config)) {
            throw new Exception("cacluate percentIncome: parse incomeConfig fail");
        }
        // 计算百分比值
        BigDecimal percentVal = new BigDecimal(config.getValue()).
                divide(new BigDecimal(100)).setScale(4, RoundingMode.HALF_UP);
        // 计算收益
        BigDecimal income = new BigDecimal(countData);
        income = income.add(income.multiply(percentVal)).setScale(0, RoundingMode.HALF_UP);
        logger.debug("caculate income end, result data:【{}】", countData);
        return income.toString();
    }
}

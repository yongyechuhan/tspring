package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Data
public abstract class ExpressIncomeCaculator {

    public static final Logger logger = LoggerFactory.getLogger(ExpressIncomeCaculator.class);

    protected String incomeConfig;

    public ExpressIncomeCaculator() {}

    public ExpressIncomeCaculator(String incomeConfig) {
        setIncomeConfig(incomeConfig);
    }

    @Data
    public static class IncomeConfig {
        private String type;
    }

    @AllArgsConstructor
    private enum IncomeTypeEnum {
        FIXED("fixed"), PERCENT("percent");

        private String type;
    }

    public static IncomeConfig parseIncome(String incomeConfig) throws Exception {
        if (StringUtils.isEmpty(incomeConfig)) {
            throw new Exception("cacluate income： income can't empty");
        }

        return JSON.parseObject(incomeConfig, IncomeConfig.class);
    }

    public abstract String caculateIncome(String countData) throws Exception;

    public static ExpressIncomeCaculator getCaculatorInstance(String incomeConfig) throws Exception {
        IncomeConfig config = ExpressIncomeCaculator.parseIncome(incomeConfig);

        if (Objects.isNull(config)) {
            throw new Exception("cacluate income: parse income config fail,");
        }

        String incomeType = config.getType();

        ExpressIncomeCaculator expressIncomeCaculator = null;
        if (IncomeTypeEnum.FIXED.type.equalsIgnoreCase(incomeType)) {
            expressIncomeCaculator = new FixedIncomeCaculator(incomeConfig);
        }

        if (IncomeTypeEnum.PERCENT.type.equalsIgnoreCase(incomeType)) {
            expressIncomeCaculator = new PercentIncomeCaculator(incomeConfig);
        }

        if (Objects.nonNull(expressIncomeCaculator)) {
            return expressIncomeCaculator;
        }

        throw new Exception("cacluate income: not support income type 【" + incomeType + "】");
    }
}

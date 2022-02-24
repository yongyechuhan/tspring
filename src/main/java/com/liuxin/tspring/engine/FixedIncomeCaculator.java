package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

public class FixedIncomeCaculator extends ExpressIncomeCaculator {

    FixedIncomeCaculator () {
        super();
    }

    FixedIncomeCaculator (String incomeConfig) {
        super(incomeConfig);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FixedIncomeConfig extends IncomeConfig {
        private String value;
    }

    @Override
    public String caculateIncome(String countData) throws Exception {
        FixedIncomeConfig config = JSON.parseObject(incomeConfig, FixedIncomeConfig.class);
        logger.debug("start caculate income, config is 【{}】", incomeConfig);
        if (Objects.isNull(config)) {
            throw new Exception("cacluate fixedIncome: parse incomeConfig fail");
        }
        return config.getValue();
    }
}

package com.liuxin.tspring.engine.caculator;

import com.liuxin.tspring.engine.caculator.ExpressIncomeCaculator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class FixedIncomeCaculator extends ExpressIncomeCaculator {

    @Override
    public Double caculateIncome(String countData) throws Exception {
        String value = getValue();
        logger.info("start caculate income, value is 【{}】", value);
        if (StringUtils.isEmpty(value)) {
            throw new Exception("cacluate fixedIncome: parse incomeConfig fail");
        }
        logger.info("caculate income end, value is 【{}】", value);
        return Double.valueOf(value);
    }
}

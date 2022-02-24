package com.liuxin.tspring.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ExpressIncomeCaculator {

    public static final Logger logger = LoggerFactory.getLogger(ExpressIncomeCaculator.class);

    /**
     * 奖励值
     */
    private String value;

    public abstract Double caculateIncome(String countData) throws Exception;
}

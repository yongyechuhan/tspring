package com.liuxin.tspring.engine.parser;

import com.liuxin.tspring.engine.bean.ExpressGroup;
import lombok.Data;

import java.util.List;

@Data
public abstract class RuleExpressParser {
    public static final String COUNT_DATA_KEY = "#countData";

    @Data
    public static class ExpressIncomeConfig {
        private String value;
        private boolean repeat;
    }

    public abstract List<ExpressGroup> parseRuleExpress(String expressIncomeConfig, boolean repeat) throws Exception;
}

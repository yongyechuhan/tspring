package com.liuxin.tspring.engine.parser;

import com.liuxin.tspring.engine.bean.ExpressGroup;
import lombok.Data;

import java.util.List;

@Data
public abstract class RuleExpressParser {
    private String ruleType;
    private String ruleItem;
    private String expressIncomeConfig;
    private boolean repeat;

    public static final String COUNT_DATA_KEY = "#countData";

    @Data
    public static class ExpressIncomeConfig {
        private String value;
    }

    public abstract List<ExpressGroup> parseRuleExpress() throws Exception;
}

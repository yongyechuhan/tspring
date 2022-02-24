package com.liuxin.tspring.engine;

import lombok.Data;

import java.util.List;

@Data
public abstract class RuleExpressParser {
    private String ruleType;
    private String ruleItem;
    private String ruleConfig;

    @Data
    public static class RuleConfig {
        private boolean repeat;
        private List<ExpressIncomeConfig> config;
    }

    @Data
    public static class ExpressIncomeConfig {
        private String express;
        private String income;
    }

    @Data
    public static class ExpressConfig {
        private String type;
    }

    abstract List<ExpressGroup> parseRuleExpress() throws Exception;
}

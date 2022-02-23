package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public abstract class RuleExpressParser {
    private String ruleType;
    private String ruleItem;
    private String ruleConfig;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleConfig {
        private boolean repeat;
        private String config;
    }

    abstract List<ExpressGroup> parseRuleExpress();
}

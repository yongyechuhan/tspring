package com.liuxin.tspring.engine.parser;

import com.alibaba.fastjson.JSON;
import com.liuxin.tspring.engine.RuleFactory;
import com.liuxin.tspring.engine.bean.ExpressGroup;
import com.liuxin.tspring.engine.bean.ExpressSubGroup;
import com.liuxin.tspring.engine.caculator.ExpressIncomeCaculator;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateRuleExpressParser extends RuleExpressParser {

    private static final Logger logger = LoggerFactory.getLogger(LadderRuleExpressParser.class);

    @Data
    public static class StateExpressConfig extends ExpressIncomeConfig {
        private String state;
    }

    @Override
    public List<ExpressGroup> parseRuleExpress(String expressIncomeConfig, boolean repeat) throws Exception {
        List<StateExpressConfig> stateExpressConfigs =
                JSON.parseArray(expressIncomeConfig, StateExpressConfig.class);

        List<ExpressGroup> expressGroups = new ArrayList<>();
        for (StateExpressConfig stateExpressConfig : stateExpressConfigs) {
            List<ExpressSubGroup> subGroups = new ArrayList<>();
            int groupCode = 0;
            ExpressSubGroup expressSubGroup = new ExpressSubGroup(COUNT_DATA_KEY, ".equals(\"", stateExpressConfig.getState().concat("\")"), groupCode);
            subGroups.add(expressSubGroup);
            String incomeCaculatorType = "fixed";
            // 尝试初始化收益执行类
            ExpressIncomeCaculator incomeCaculator = RuleFactory.getInstance(ExpressIncomeCaculator.class.getName(), incomeCaculatorType);
            incomeCaculator.setValue(stateExpressConfig.getValue());
            ExpressGroup expressGroup = new ExpressGroup(subGroups, new HashMap<>(), incomeCaculator);
            expressGroups.add(expressGroup);
        }
        return expressGroups;
    }
}

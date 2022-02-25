package com.liuxin.tspring.engine.parser;

import com.alibaba.fastjson.JSON;
import com.liuxin.tspring.engine.RuleEngine;
import com.liuxin.tspring.engine.RuleFactory;
import com.liuxin.tspring.engine.bean.EventActionBean;
import com.liuxin.tspring.engine.bean.ExpressGroup;
import com.liuxin.tspring.engine.bean.ExpressSubGroup;
import com.liuxin.tspring.engine.caculator.ExpressIncomeCaculator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class LadderRuleExpressParser extends RuleExpressParser {

    private static final Logger logger = LoggerFactory.getLogger(LadderRuleExpressParser.class);

    @Data
    public static class LadderExpressConfig extends ExpressIncomeConfig {
        private String min;
        private String max;
        private String type;
    }

    @Override
    public List<ExpressGroup> parseRuleExpress(String expressIncomeConfig, boolean repeat) throws Exception {
        List<LadderExpressConfig> ladderExpressConfigs =
                JSON.parseArray(expressIncomeConfig, LadderExpressConfig.class);

        List<ExpressGroup> expressGroups = new ArrayList<>();
        for (LadderExpressConfig ladderExpressConfig : ladderExpressConfigs) {
            Map<Integer, Queue<String>> symbolQueue = new LinkedHashMap<>();
            List<ExpressSubGroup> subGroups = new ArrayList<>();
            int groupCode = 0;
            Queue<String> queue = new ArrayDeque();
            if (StringUtils.isNotEmpty(ladderExpressConfig.getMin())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", ladderExpressConfig.getMin(), groupCode));
            }

            if (StringUtils.isNotEmpty(ladderExpressConfig.getMax())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, "<=", ladderExpressConfig.getMax(), groupCode));
            }

            if (subGroups.size() > 1) {
                queue.add("and");
                symbolQueue.put(groupCode, queue);

                if (repeat) {
                    queue.add("or");
                    subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", ladderExpressConfig.getMax(), ++groupCode));
                }
            }

            String incomeCaculatorType = ladderExpressConfig.getType();
            // 尝试初始化收益执行类
            ExpressIncomeCaculator incomeCaculator = RuleFactory.getInstance(ExpressIncomeCaculator.class.getName(), incomeCaculatorType);
            incomeCaculator.setValue(ladderExpressConfig.getValue());
            ExpressGroup expressGroup = new ExpressGroup(subGroups, symbolQueue, incomeCaculator);
            expressGroups.add(expressGroup);
        }
        return expressGroups;
    }

    public static void main(String[] args) {
        List<String> ruleConfigs = new ArrayList<>();
        String r1 = "{\"repeat\": true, \"config\": {\"type\": \"ladder\", \"express\":[{\"max\":\"100\",\"min\":\"10\", \"type\":\"percent\",\"value\":\"80\"}," +
                "{\"max\":\"500\",\"min\":\"100\", \"type\":\"fixed\",\"value\":\"100\"}," +
                "{\"max\":\"1000\",\"min\":\"500\", \"type\":\"percent\",\"value\":\"100\"}," +
                "{\"min\":\"2000\", \"type\":\"fixed\",\"value\":\"1000\"}]}}";
        String r2 = "{\"repeat\": false, \"config\": {\"type\": \"ladder\", \"express\":[{\"max\":\"100\",\"min\":\"10\",\"type\":\"percent\",\"value\":\"90\"}]}}";
        String r3 = "{\"repeat\": false, \"config\": {\"type\": \"state\", \"express\":[{\"state\":\"优秀\",\"value\":\"100\"}," +
                "{\"state\":\"良好\",\"value\":\"80\"}," +
                "{\"state\":\"及格\",\"value\":\"60\"}]}}";
        ruleConfigs.add(r1);
        ruleConfigs.add(r2);
        ruleConfigs.add(r3);

        RuleEngine ruleEngine = new RuleEngine();
        EventActionBean eventActionBean = new EventActionBean();
        eventActionBean.setData("100");
        ruleEngine.engineWork(eventActionBean, ruleConfigs);
    }
}
package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class LadderRuleExpressParser extends RuleExpressParser {

    private static final Logger logger = LoggerFactory.getLogger(LadderRuleExpressParser.class);

    @Data
    public static class LadderExpressConfig extends ExpressConfig {
        private String min;
        private String max;
    }

    @Override
    public List<ExpressGroup> parseRuleExpress() throws Exception {
        RuleConfig ruleConfig = JSON.parseObject(getRuleConfig(), RuleConfig.class);
        List<ExpressIncomeConfig> expressIncomeConfigs = ruleConfig.getConfig();

        List<ExpressGroup> expressGroups = new ArrayList<>();
        for (ExpressIncomeConfig expressIncomeConfig : expressIncomeConfigs) {
            String express = expressIncomeConfig.getExpress();
            LadderExpressConfig expressConfig = JSON.parseObject(express, LadderExpressConfig.class);
            Map<Integer, Queue<String>> symbolQueue = new LinkedHashMap<>();
            List<ExpressSubGroup> subGroups = new ArrayList<>();
            int groupCode = 0;
            Queue<String> queue = new ArrayDeque();
            if (StringUtils.isNotEmpty(expressConfig.getMin())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", expressConfig.getMin(), groupCode));
            }

            if (StringUtils.isNotEmpty(expressConfig.getMax())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, "<=", expressConfig.getMax(), groupCode));
            }

            if (subGroups.size() > 1) {
                queue.add("and");
                symbolQueue.put(groupCode, queue);

                if (ruleConfig.isRepeat()) {
                    queue.add("or");
                    subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", expressConfig.getMax(), ++groupCode));
                }
            }

            String income = expressIncomeConfig.getIncome();
            // 尝试初始化收益执行类
            ExpressIncomeCaculator incomeCaculator = ExpressIncomeCaculator.getCaculatorInstance(income);
            ExpressGroup expressGroup = new ExpressGroup(subGroups, symbolQueue, incomeCaculator);
            expressGroups.add(expressGroup);
        }
        return expressGroups;
    }

    public static void main(String[] args) {
//        RuleExpressParser expressParser = new LadderRuleExpressParser();
//        String ruleConfig = "{\"repeat\": true, \"config\": [{\"income\":{\"type\":\"fixed\",\"value\":\"100\"},\"max\":\"100\",\"min\":\"10\"},{\"income\":{\"type\":\"percent\",\"value\":\"80\"},\"max\":\"600\",\"min\":\"200\"},{\"income\":{\"type\":\"fixed\",\"value\":\"200\"}, \"min\":\"1000\"}]}";
//        String ruleConfig = "{\"repeat\": true, \"config\": [{\"income\":{\"type\":\"percent\",\"value\":\"80\"},\"express\":{\"max\":\"100\",\"min\":\"10\"}}]}";
//        expressParser.setRuleConfig(ruleConfig);
//        List<ExpressGroup> expressGroups = null;
//        RuleExpress ruleExpress = null;
//        try {
//            expressGroups = expressParser.parseRuleExpress();
//            ruleExpress = RuleExpress.build(expressGroups);
//        } catch (Exception e) {
//            logger.error("parse express fail", e);
//        }
//
//        Double countData = 320.2D;
//
//        for (int i = 0; i < ruleExpress.getExpressList().size(); i++) {
//            RuleExpress.RuleExpressIncomeInstance instance = ruleExpress.getExpressList().get(i);
//            String express = instance.getExpress();
//            logger.info(express);
//            boolean expressResult = RuleExpressChecker.check(express, countData);
//            logger.info(String.valueOf(expressResult));
//            if (expressResult) {
//                try {
//                    String value = instance.getExpressIncomeCaculator().caculateIncome(String.valueOf(countData));
//                    logger.info("激励值：【{}】", value);
//                } catch (Exception e) {
//                    logger.error("caculate income fail", e);
//                }
//            }
//        }
        RuleEngine ruleEngine = new RuleEngine();
        ruleEngine.preFilter(new EventActionBean());
    }
}

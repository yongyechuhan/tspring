package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class LadderRuleExpressParser extends RuleExpressParser {

    private static final String COUNT_DATA_KEY = "#countData";

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
        RuleExpressParser expressParser = new LadderRuleExpressParser();
//        String ruleConfig = "{\"repeat\": true, \"config\": [{\"income\":{\"type\":\"fixed\",\"value\":\"100\"},\"max\":\"100\",\"min\":\"10\"},{\"income\":{\"type\":\"percent\",\"value\":\"80\"},\"max\":\"600\",\"min\":\"200\"},{\"income\":{\"type\":\"fixed\",\"value\":\"200\"}, \"min\":\"1000\"}]}";
        String ruleConfig = "{\"repeat\": true, \"config\": [{\"income\":{\"type\":\"fixed\",\"value\":\"80\"},\"express\":{\"max\":\"100\",\"min\":\"10\"}}]}";
        expressParser.setRuleConfig(ruleConfig);
        List<ExpressGroup> expressGroups = null;
        RuleExpress ruleExpress = null;
        try {
            expressGroups = expressParser.parseRuleExpress();
            ruleExpress = RuleExpress.build(expressGroups);
        } catch (Exception e) {
            logger.error("parse express fail", e);
        }

        String countData = "-1";

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("countData", Integer.valueOf(countData));
        ExpressionParser parser = new SpelExpressionParser();
        for (int i = 0; i < ruleExpress.getExpressList().size(); i++) {
            RuleExpress.RuleExpressIncomeInstance instance = ruleExpress.getExpressList().get(i);
            Expression expression = parser.parseExpression(instance.getExpress());
            logger.info(instance.getExpress());
            boolean expressResult = expression.getValue(context, boolean.class);
            logger.info(JSON.toJSONString(expressResult));
            if (expressResult) {
                try {
                    String value = instance.getExpressIncomeCaculator().caculateIncome(countData);
                    logger.info("激励值：【{}】", value);
                } catch (Exception e) {
                    logger.error("caculate income fail", e);
                }
            }
        }
    }
}

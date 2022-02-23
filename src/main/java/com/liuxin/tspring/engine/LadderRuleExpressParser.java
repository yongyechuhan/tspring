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
    public static class LadderConfig {
        private String min;
        private String max;
        private LadderIncome income;
    }

    @Data
    public static class LadderIncome {
        private String type;
        private String value;
    }

    @Override
    public List<ExpressGroup> parseRuleExpress() {
        RuleConfig ruleConfig = JSON.parseObject(getRuleConfig(), RuleConfig.class);
        List<LadderConfig> ladderConfigs = JSON.parseArray(ruleConfig.getConfig(), LadderConfig.class);
        List<ExpressGroup> expressGroups = ladderConfigs.stream().map(ladderConfig -> {
            Map<Integer, Queue<String>> symbolQueue = new LinkedHashMap<>();
            List<ExpressSubGroup> subGroups = new ArrayList<>();
            int groupCode = 0;
            Queue<String> queue = new ArrayDeque();
            if (StringUtils.isNotEmpty(ladderConfig.getMin())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", ladderConfig.getMin(), groupCode));
            }

            if (StringUtils.isNotEmpty(ladderConfig.getMax())) {
                subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, "<=", ladderConfig.getMax(), groupCode));
            }

            if (subGroups.size() > 1) {
                queue.add("and");
                symbolQueue.put(groupCode, queue);

                if (ruleConfig.isRepeat()) {
                    queue.add("or");
                    subGroups.add(new ExpressSubGroup(COUNT_DATA_KEY, ">", ladderConfig.getMax(), ++groupCode));
                }
            }

            ExpressGroup expressGroup = new ExpressGroup(subGroups, symbolQueue);
            return expressGroup;
        }).collect(Collectors.toList());
        return expressGroups;
    }

    public static void main(String[] args) {
        RuleExpressParser expressParser = new LadderRuleExpressParser();
        String ruleConfig = "{\"repeat\": true, \"config\": [{\"income\":{\"type\":\"fixed\",\"value\":\"100\"},\"max\":\"100\",\"min\":\"10\"},{\"income\":{\"type\":\"percent\",\"value\":\"80\"},\"max\":\"600\",\"min\":\"200\"},{\"income\":{\"type\":\"fixed\",\"value\":\"200\"}, \"min\":\"1000\"}]}";
        expressParser.setRuleConfig(ruleConfig);
        List<ExpressGroup> expressGroups = expressParser.parseRuleExpress();
        RuleExpress ruleExpress = RuleExpress.build(expressGroups);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("countData", 30000000);
        ExpressionParser parser = new SpelExpressionParser();
        for (int i = 0; i < ruleExpress.getExpressList().size(); i++) {
            Expression expression = parser.parseExpression(ruleExpress.getExpressList().get(i));
            logger.info(ruleExpress.getExpressList().get(i));
            logger.info(JSON.toJSONString(expression.getValue(context, boolean.class)));
        }
    }
}

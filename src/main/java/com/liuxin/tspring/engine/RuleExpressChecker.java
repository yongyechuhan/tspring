package com.liuxin.tspring.engine;

import com.liuxin.tspring.engine.parser.RuleExpressParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

public class RuleExpressChecker {

    public static boolean check(String express, String countData) {
        Assert.notNull(express);
        Assert.notNull(countData);

        EvaluationContext context = new StandardEvaluationContext();
        String dataKey = RuleExpressParser.COUNT_DATA_KEY.replace("#", "");

        if (StringUtils.isNumeric(countData)) {
            context.setVariable(dataKey, Double.valueOf(countData));
        } else {
            context.setVariable(dataKey, countData);
        }

        ExpressionParser parser = new SpelExpressionParser();


        try {
            Expression expression = parser.parseExpression(express);
            return expression.getValue(context, Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

}

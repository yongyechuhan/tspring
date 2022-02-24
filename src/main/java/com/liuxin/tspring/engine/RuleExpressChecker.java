package com.liuxin.tspring.engine;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

public class RuleExpressChecker {

    public static boolean check(String express, Double countData) {
        Assert.notNull(express);
        Assert.notNull(countData);

        EvaluationContext context = new StandardEvaluationContext();
        String dataKey = RuleExpressParser.COUNT_DATA_KEY.replace("#", "");
        context.setVariable(dataKey, countData);
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(express);
        return expression.getValue(context, Boolean.class);
    }

}

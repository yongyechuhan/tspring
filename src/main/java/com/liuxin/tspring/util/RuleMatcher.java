package com.liuxin.tspring.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class RuleMatcher {

    private static Logger logger = Logger.getLogger(RuleMatcher.class.toString());

    abstract boolean matcher(Order order, String expressStr);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Order {
        private long orderNum;
        private long orderAmount;
        private int shopLevel;
    }

    private static class StandardRuleMatcher extends RuleMatcher {
        public boolean matcher(Order order, String expressStr) {
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("order", order);
            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(expressStr);
            return expression.getValue(context, boolean.class);
        }
    }

    private static class DataReciveConsumer {
        public static Order reciveData() {
            Callable<Order> callable = () -> {
                try {
                    Thread.sleep(new Random().nextInt(10) * 500 + 200);
                    long orderNum = new Random().nextInt(200);
                    long orderAmount = new Random().nextInt(2000);
                    int shopLevel = new Random().nextInt(10);
                    Order order = new Order(orderNum, orderAmount, shopLevel);
                    logger.info("current order count data: " + JSON.toJSONString(order));
                    return order;
                } catch (InterruptedException e) {
                    logger.warning("sleep exception " +e.getMessage());
                }
                return null;
            };
            try {
                return callable.call();
            } catch (Exception e) {
                logger.warning("call exception "+e.getMessage());
            }
            return null;
        }
    }

    @Data
    private static abstract class Rule {
        private String express;
        abstract boolean checkDataMatchSelf(Order order);
    }

    private static class StandardRule extends Rule {
        private static final String express = "(#order.orderNum > 100 and #order.orderAmount > 1000) or #order.shopLevel > 6";

        StandardRule () {
            super.setExpress(express);
        }

        public boolean checkDataMatchSelf(Order order) {
            RuleMatcher ruleMatcher = new StandardRuleMatcher();
            boolean ruleMatchResult = ruleMatcher.matcher(order, express);
            return ruleMatchResult;
        }
    }

    static class RuleEngine {
        public void startEngine() {
            while(true) {
                // 接受数据变化
                Order order = DataReciveConsumer.reciveData();
                // 模拟规则集合
                List<Rule> rules =
                        Arrays.asList(new StandardRule()).stream().filter(rule -> rule.checkDataMatchSelf(order)).collect(Collectors.toList());
                logger.info("match rules :"+JSON.toJSONString(rules));
            }
        }
    }

    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();
        engine.startEngine();
    }
}

package com.liuxin.tspring.engine;

import com.alibaba.fastjson.JSON;
import com.liuxin.tspring.engine.bean.EventActionBean;
import com.liuxin.tspring.engine.bean.ExpressGroup;
import com.liuxin.tspring.engine.bean.RuleExpress;
import com.liuxin.tspring.engine.filter.RuleCheckPreFilter;
import com.liuxin.tspring.engine.filter.TimeScopeFilter;
import com.liuxin.tspring.engine.filter.UserAcceptFilter;
import com.liuxin.tspring.engine.parser.RuleExpressParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * 规则引擎主类
 * <p>
 * 接收行为数据对行为进行规则匹配和激励下发
 */
public class RuleEngine {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    private static final Queue<RuleCheckPreFilter> preFilters = new ArrayDeque<>();

    @Data
    public static class RuleConfig {
        private boolean repeat;
        private ExpressIncomeConfig config;
    }

    @Data
    public static class ExpressIncomeConfig {
        private String type;
        private String express;
    }

    /**
     * 规则过滤器，按照过滤器链依次检查适用触发事件的规则
     *
     * @param eventAction
     */
    public void preFilter(EventActionBean eventAction) {
        if (CollectionUtils.isEmpty(preFilters)) {
            synchronized (this.getClass()) {
                if (CollectionUtils.isEmpty(preFilters)) {
                    preFilters.add(new TimeScopeFilter());
                    preFilters.add(new UserAcceptFilter());

                    logger.info("rule check pre filter init success");
                }
            }
        }
        //TODO 按照规则类型和规则项目查询规则明细
        String rule = "{\"userConfig\":{\"scopeType\":\"organize\", \"organizeType\":\"大区\", \"organizeName\":\"华东区\", \"userExtends\":\"店长\"}," +
                "\"timeConfig\":{\"scopeType\":\"scope\",\"startTime\":\"2088-01-10 00:00:00\", \"endTime\":\"2099-02-01 23:59:59\"}}";
        RuleCheckPreFilter checkPreFilter = preFilters.poll();
        checkPreFilter.filter(eventAction, rule, preFilters);
    }

    /**
     * 经过前置过滤器过滤出所有符合行为的规则列表后，开始执行激励匹配和激励执行
     *
     * @param eventAction
     */
    public void engineWork(EventActionBean eventAction, List<String> rules) {
        // TODO 提取出规则中关于奖励的配置
        Map<String, Double> ruleIncomeMap = rules.stream().collect(Collectors.toMap(rule -> rule, rule -> {
            try {
                RuleConfig ruleConfig = JSON.parseObject(rule, RuleConfig.class);
                // 获取奖励类型对应的规则解析器
                RuleExpressParser expressParser = RuleFactory.getInstance(RuleExpressParser.class.getName(),
                        ruleConfig.getConfig().getType());
                // 将奖励配置设置到解析器中
                expressParser.setExpressIncomeConfig(ruleConfig.getConfig().getExpress());
                expressParser.setRepeat(ruleConfig.isRepeat());

                // 执行解析,构造引擎需要的表达式结构，并设置表达式对应的激励计算对象
                List<ExpressGroup> expressGroups = expressParser.parseRuleExpress();
                if (CollectionUtils.isEmpty(expressGroups)) {
                    logger.error("parse express groups with rule {} fail", rule);
                    return 0D;
                }
                // 构造可执行的表达式处理对象
                RuleExpress ruleExpress = RuleExpress.build(expressParser.parseRuleExpress());
                // 校验表达式是否成立，累计所有成立表达式的收益
                return ruleExpress.getExpressList().stream().filter(express ->
                        RuleExpressChecker.check(express.getExpress(), Double.valueOf(eventAction.getData()))
                ).map(express -> {
                    try {
                        return express.getExpressIncomeCaculator().
                                caculateIncome(eventAction.getData());
                    } catch (Exception e) {
                        logger.error("caculate express income fail, express:{} data:{}", express.getExpress(), eventAction.getData());
                        return 0D;
                    }
                }).collect(Collectors.summingDouble(income -> income));
            } catch (Exception e) {
                logger.error("caculate rule income fail", e);
                return 0D;
            }
        }));

        logger.info(JSON.toJSONString(ruleIncomeMap.values()));
    }
}

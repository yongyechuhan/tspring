package com.liuxin.tspring.engine;

import com.liuxin.tspring.engine.filter.RuleCheckPreFilter;
import com.liuxin.tspring.engine.filter.TimeScopeFilter;
import com.liuxin.tspring.engine.filter.UserAcceptFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 规则引擎主类
 * <p>
 * 接收行为数据对行为进行规则匹配和激励下发
 */
public class RuleEngine {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    private static final Queue<RuleCheckPreFilter> preFilters = new ArrayDeque<>();

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
        //TODO
    }

    public void engineWork(EventActionBean eventAction) {

    }
}

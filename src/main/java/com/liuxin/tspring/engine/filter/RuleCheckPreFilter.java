package com.liuxin.tspring.engine.filter;

import com.liuxin.tspring.engine.bean.EventActionBean;

import java.util.Objects;
import java.util.Queue;

public abstract class RuleCheckPreFilter {
    public abstract boolean filter(EventActionBean eventAction, String rule, Queue<RuleCheckPreFilter> preFilters);

    protected boolean filterChain(EventActionBean eventAction, String rule, Queue<RuleCheckPreFilter> preFilters, boolean filterResult) {
        RuleCheckPreFilter checkPreFilter = preFilters.poll();
        if (!filterResult || Objects.isNull(checkPreFilter)) {
            return false;
        }

        return checkPreFilter.filter(eventAction, rule, preFilters);
    }
}

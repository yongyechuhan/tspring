package com.liuxin.tspring.engine.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuxin.tspring.engine.bean.EventActionBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

public class TimeScopeFilter extends RuleCheckPreFilter {

    private static final Logger logger = LoggerFactory.getLogger(TimeScopeFilter.class);

    @Data
    public static class TimeScope {
        private String scopeType;
        private String startTime;
        private String endTime;
    }

    public boolean filter(EventActionBean eventAction, String rule, Queue<RuleCheckPreFilter> preFilters) {
        Assert.notNull(rule);

        boolean filterResult;
        try {
            String timeConfig = JSON.parseObject(rule, JSONObject.class).getString("timeConfig");
            TimeScope timeScope = JSON.parseObject(timeConfig, TimeScope.class);
            if ("VALID".equalsIgnoreCase(timeScope.getScopeType())) {
                filterResult = true;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date start = dateFormat.parse(timeScope.getStartTime());
                Date end = dateFormat.parse(timeScope.getEndTime());

                long timeMillions = new Date().getTime();
                filterResult = ((timeMillions - start.getTime()) >= 0 && (timeMillions - end.getTime()) <= 0);
            }
        } catch (Exception e) {
            logger.error("filter rule with time fail", e);
            filterResult = false;
        }

        logger.info("time scope filter end, result:{}", filterResult);
        return filterChain(eventAction, rule, preFilters, filterResult);
    }
}

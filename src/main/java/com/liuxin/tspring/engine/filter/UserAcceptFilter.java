package com.liuxin.tspring.engine.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuxin.tspring.engine.EventActionBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Queue;

/**
 * 用于校验触发事件的用户是否在规则设置的适应用户范围之内
 */
public class UserAcceptFilter extends RuleCheckPreFilter {

    private static final Logger logger = LoggerFactory.getLogger(UserAcceptFilter.class);

    @Data
    public static class ScopeCond {
        private String scopeType;
    }

    @Data
    public static class GroupCond extends ScopeCond {
        private String groupId;
    }

    @Data
    public static class OrganizeCond extends ScopeCond {
        /**
         * 组织类型
         */
        private String organzieType;
        /**
         * 组织名称
         */
        private String organizeName;
        /**
         * 附加用户属性
         */
        private String userExtends;
    }

    public boolean filter(EventActionBean eventAction, String rule, Queue<RuleCheckPreFilter> preFilters) {
        boolean filterResult;
        try {
            String wid = eventAction.getWid();
            String userConfig = JSON.parseObject(rule, JSONObject.class).getString("userConfig");
            ScopeCond scopeCond = JSON.parseObject(userConfig, ScopeCond.class);
            if ("organize".equalsIgnoreCase(scopeCond.getScopeType())) {
                filterResult = checkUserBelongToOrganize(wid, JSON.parseObject(rule, OrganizeCond.class));
            } else {
                filterResult = checkUserBelongToUserGroup(wid, JSON.parseObject(rule, GroupCond.class));
            }

        } catch (Exception e) {
            logger.error("invoke api check user is in scope fail", e);
            filterResult = false;
        }

        logger.info("user scope filter end, result:{}", filterResult);
        return filterChain(eventAction, rule, preFilters, filterResult);
    }

    private static boolean checkUserBelongToOrganize(String wid, OrganizeCond organize) {
        return true;
    }

    private static boolean checkUserBelongToUserGroup(String wid, GroupCond groupCond){
        return true;
    }
}

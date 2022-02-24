package com.liuxin.tspring.engine.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件行为数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventActionBean {
    /**
     * 事件来源
     */
    private String eventSource;
    /**
     * 事件发生时间
     */
    private String eventTime;
    /**
     * 可以作为唯一识别原始消息的ID
     */
    private String eventId;
    /**
     * 事件触发的规则类型
     */
    private String ruleType;
    /**
     * 事件触发的规则项目
     */
    private String ruleItem;
    /**
     * 行为人的账号ID
     */
    private String wid;
    /**
     * 行为数据
     */
    private String data;
}

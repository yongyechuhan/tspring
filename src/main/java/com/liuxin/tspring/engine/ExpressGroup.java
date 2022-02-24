package com.liuxin.tspring.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressGroup {
    /**
     * 一个逻辑表达式分解出的表达式组
     */
    private List<ExpressSubGroup> subGroups;
    /**
     * 表达式组拼接符号
     * Integer 组号
     * Queue<String> 对应组的拼接符号队列
     */
    private Map<Integer, Queue<String>> connSymbolQueue;
    /**
     * 解析出的
     */
    private ExpressIncomeCaculator incomeCaculator;
}

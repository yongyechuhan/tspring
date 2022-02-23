package com.liuxin.tspring.engine;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class RuleExpress {
    private String expressId;
    private int order;
    private Set<String> mutexRules;
    private List<String> expressList;

    public static RuleExpress build(List<ExpressGroup> expressGroup) {
        List<String> expressList = expressGroup.stream().map(group -> {
            List<ExpressSubGroup> subGroups = group.getSubGroups();
            StringBuffer expressBuffer = new StringBuffer("");
            expressBuffer = buildExpress(expressBuffer, 0, subGroups, group.getConnSymbolQueue());
            return expressBuffer.toString();
        }).collect(Collectors.toList());

        RuleExpress ruleExpress = new RuleExpress();
        ruleExpress.setExpressList(expressList);
        return ruleExpress;
    }

    public static StringBuffer buildExpress(StringBuffer preGroupExpress, int groupCode, List<ExpressSubGroup> expressSubGroups, Map<Integer, Queue<String>> queueMap) {
        final int codeFinal = groupCode;
        List<ExpressSubGroup> filterGroup =
                expressSubGroups.stream().filter(subGroup -> subGroup.getGroupCode() == codeFinal).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterGroup)) {
            return preGroupExpress;
        }
        Queue queue = queueMap.get(groupCode);
        int index = 0;
        StringBuffer currentBuffer = new StringBuffer("");
        for (ExpressSubGroup singleExpress : filterGroup) {
            currentBuffer = currentBuffer.append(singleExpress.getCompareVariable().concat(singleExpress.getCompareSymbol()).concat(singleExpress.getCompareValue()));
            Object symbol;
            if (index != filterGroup.size() - 1 && Objects.nonNull(queue) && Objects.nonNull(symbol = queue.poll())) {
                currentBuffer.append(" ").append(symbol).append(" ");
            }
            index++;
        }
        currentBuffer = new StringBuffer("(").append(currentBuffer).append(")");
        if (preGroupExpress.length() > 0) {
            preGroupExpress = new StringBuffer("(").append(preGroupExpress.append(currentBuffer)).append(")");
        } else {
            preGroupExpress = currentBuffer;
        }
        Object symbol;
        if (Objects.nonNull(queue) && Objects.nonNull(symbol = queue.poll())) {
            preGroupExpress.append(" "+symbol+" ");
        }

        if (groupCode < queueMap.size()) {
            preGroupExpress = buildExpress(preGroupExpress, ++groupCode, expressSubGroups, queueMap);
        }

        return preGroupExpress;
    }
}


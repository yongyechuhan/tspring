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
    private List<ExpressSubGroup> subGroups;
    private Map<Integer, Queue<String>> connSymbolQueue;
}

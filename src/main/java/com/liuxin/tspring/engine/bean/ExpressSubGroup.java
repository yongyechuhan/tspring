package com.liuxin.tspring.engine.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressSubGroup {
    private String compareVariable;
    private String compareSymbol;
    private String compareValue;
    private int groupCode;
}

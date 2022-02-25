package com.liuxin.tspring.engine;

import com.liuxin.tspring.engine.caculator.ExpressIncomeCaculator;
import com.liuxin.tspring.engine.caculator.FixedIncomeCaculator;
import com.liuxin.tspring.engine.caculator.PercentIncomeCaculator;
import com.liuxin.tspring.engine.parser.LadderRuleExpressParser;
import com.liuxin.tspring.engine.parser.RuleExpressParser;
import com.liuxin.tspring.engine.parser.StateRuleExpressParser;

public class RuleFactory {
    public static <T> T getInstance(String className, String type) throws Exception {
        if (RuleExpressParser.class.getName().equalsIgnoreCase(className)) {
            switch (type) {
                case "ladder": return (T) new LadderRuleExpressParser();
                case "state": return (T) new StateRuleExpressParser();
            }

            throw new Exception("unhandler type "+type+" with build RuleExpressParser");
        }

        if (ExpressIncomeCaculator.class.getName().equalsIgnoreCase(className)) {
            switch (type) {
                case "fixed": return (T) new FixedIncomeCaculator();
                case "percent": return (T) new PercentIncomeCaculator();
            }

            throw new Exception("unhandler type "+type+" with build ExpressIncomeCaculator");
        }

        return null;
    }
}

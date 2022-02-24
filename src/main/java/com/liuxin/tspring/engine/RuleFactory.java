package com.liuxin.tspring.engine;

public class RuleFactory {
    public static <T> T getInstance(String className, String type) throws Exception {
        if (RuleExpressParser.class.getName().equalsIgnoreCase(className)) {
            switch (type) {
                case "ladder": return (T) new LadderRuleExpressParser();
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

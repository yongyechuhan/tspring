package com.liuxin.tspring.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by 公司 on 2017/2/17.
 */
public class AbsBusi {
    protected static Log getLog(Class implClass){
        return LogFactory.getLog(implClass);
    }
}

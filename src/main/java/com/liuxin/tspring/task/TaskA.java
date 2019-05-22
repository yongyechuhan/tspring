package com.liuxin.tspring.task;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2016/12/18.
 */
@Service
public class TaskA extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(TaskA.class);

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("我是任务A");
    }
}

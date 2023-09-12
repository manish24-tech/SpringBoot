package com.example.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class JobCompletionListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {

        String jobId = jobExecution.getJobParameters().getString("jobId");
        String excelFilePath = jobExecution.getJobParameters().getString("excelPath");

        // get job's start time
        LocalDateTime start = jobExecution.getCreateTime();
        //  get job's end time
        LocalDateTime end = jobExecution.getEndTime();

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("==========JOB FINISHED=======");
            log.info("JobId      : {}", jobId);
            log.info("excel Path      : {}", excelFilePath);
            log.info("Start Date: {}", start);
            log.info("End Date: {}", end);
            log.info("==============================");
        }

    }

}
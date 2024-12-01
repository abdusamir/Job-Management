package com.prinkipia.JobManager.service;

import java.util.TimerTask;
public class ScheduleJob extends TimerTask{
    private final JobService jobService;
    private long jobId;

    public ScheduleJob(JobService jobService, Long jobId) {
        this.jobService = jobService;
        this.jobId = jobId;
    }

    @Override
    public void run() {
        jobService.runJob(jobId);
    }
}

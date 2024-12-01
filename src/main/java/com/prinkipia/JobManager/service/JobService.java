package com.prinkipia.JobManager.service;

import com.prinkipia.JobManager.model.Job;
import com.prinkipia.JobManager.model.enums.Status;
import com.prinkipia.JobManager.repository.JobRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job save(Job job) {
        job.setStatus(Status.QUEUED);
        return this.jobRepository.save(job);
    }
    public List<Job> saveAll(List<Job> jobs) {
        for(Job job:jobs){
            job.setStatus(Status.QUEUED);
        }
        return this.jobRepository.saveAll(jobs);
    }
    public Job findById(long id) {
        return this.jobRepository.findById(id).get();
    }
    public void deleteById(long id) {
        this.jobRepository.deleteById(id);
    }

    public List<Job> findAll() {
        return this.jobRepository.findAll();
    }

    @Async
    public void runScheduledJob(long id, long delay){
        ScheduleJob ScheduleJob = new ScheduleJob(this, id);
        Timer t = new Timer();
        long delayInMilliseconds = delay*1000;
        t.schedule(ScheduleJob,delayInMilliseconds);
    }


    @Async
    public void runJob(long id){
        Job job = this.jobRepository.findById(id).get();
        job.setStatus(Status.RUNNING);
        this.jobRepository.save(job);
        /* Assumption: Every created job that is running is calling external API to do the defined job.
         *  Assumption: If the API returns a failed state, the Job status should be set as FAILED.
         *  Assumption: It could be implemented in try/catch block or if statements.
         * */
        try {
            Thread.sleep(10000); /* Simulating time spent by the job. */
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ThreadLocalRandom.current().nextInt()%10!=0||ThreadLocalRandom.current().nextInt()%10!=1)
            job.setStatus(Status.SUCCESS);
        else{
            job.setStatus(Status.FAILED);
        }
        this.jobRepository.save(job);
    }

}
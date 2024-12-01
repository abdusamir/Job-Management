package com.prinkipia.JobManager.controller;

import com.prinkipia.JobManager.model.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.prinkipia.JobManager.service.JobService;
import com.prinkipia.JobManager.model.Job;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is a REST Controller used for interaction with the Job Management System.
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/addJob")
    public ResponseEntity<Job> addJob(@RequestBody Job job){
        insertTime(job);
        return ResponseEntity.ok(this.jobService.save(job));
    }
    @PostMapping("/startJob/{id}")
    public ResponseEntity<String> startJob(@PathVariable Long id) {
        this.jobService.runJob(id);
        return ResponseEntity.ok("Job Started");
    }
    @GetMapping("/checkStatus/{id}")
    public ResponseEntity<Job> checkJobStatus(@PathVariable Long id) {
        Job job = this.jobService.findById(id);
        return ResponseEntity.ok(job);
    }
    @DeleteMapping("/deleteJob/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        if (!this.jobService.findById(id).getStatus().equals(Status.RUNNING)) {
            this.jobService.deleteById(id);
            return ResponseEntity.ok("Job Deleted");
        }else{
            return ResponseEntity.badRequest().body("Job Running");
        }
    }
    @PostMapping("/addJobs")
    public ResponseEntity<List<Job>> addJob(@RequestBody List<Job> jobs){
        jobs.forEach(this::insertTime);
        return ResponseEntity.ok(this.jobService.saveAll(jobs));
    }
    @PostMapping("/startJobs")
    public ResponseEntity<String> startJobs(@RequestBody List<Long> ids) {
        for(Long id:ids)
            this.jobService.runJob(id);
        return ResponseEntity.ok("Jobs Started");
    }



    @GetMapping("/getAllJobs")
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping("/scheduleJob/{id}")
    public ResponseEntity<String> scheduleJob(@PathVariable Long id,
                                      @RequestParam Long delayInSeconds) {
        jobService.runScheduledJob(id, delayInSeconds);
        return ResponseEntity.ok("Job Scheduled");
    }

    private void insertTime(Job job){
        job.setCreatedAt(LocalDateTime.now());
    }

}
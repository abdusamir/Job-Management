package com.prinkipia.JobManager.repository;

import com.prinkipia.JobManager.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
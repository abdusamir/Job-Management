package com.prinkipia.JobManager;

import com.prinkipia.JobManager.model.Job;
import com.prinkipia.JobManager.model.enums.Status;
import com.prinkipia.JobManager.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobManagerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ObjectMapper objectMapper;

	Job job;

	@BeforeEach
	public void setup(){
		job = new Job("testName","test description", LocalDateTime.now(), Status.QUEUED);
	}


	//Save Job Test
	@Test
	@Order(1)
	public void saveJobTest() throws Exception {
		// precondition

       /* ** Precondition is in the above setup() method.
        Other test methods can access this job object instead
        create new job for each test methods */

		// Action and Verify
		mockMvc.perform(post("/jobs/addJob")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(job)))

				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.name").value("testName"))
				.andExpect(jsonPath("$.description").value("test description"))
				.andExpect(jsonPath("$.status").value("QUEUED"));
	}


	//Get All jobs Test
	@Test
	@Order(2)
	public void getAllJob() throws Exception {
		// precondition
		List<Job> jobsList = new ArrayList<>();
		jobsList.add(job);
		jobsList.add(new Job("testName","test description", LocalDateTime.now(), Status.QUEUED));
		jobRepository.saveAll(jobsList);


		// Action and Verify
		mockMvc.perform(get("/jobs/getAllJobs"))

				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",is(jobsList.size()+1)));

	}

	//Get jobById Test
	@Test
	@Order(3)
	public void getJobById() throws Exception{

		// Action and Verify
		mockMvc.perform(get("/jobs/checkStatus/{id}", 1))

				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.name", is(job.getName())))
				.andExpect(jsonPath("$.description", is(job.getDescription())))
				.andExpect(jsonPath("$.status", is("QUEUED")));

	}

	//Delete jobs Test
	@Test
	@Order(4)
	public void deleteJob() throws Exception{

		// Action and Verify
		mockMvc.perform(delete("/jobs/deleteJob/{id}", 1))
				.andExpect(status().isOk())
				.andDo(print());
	}

}

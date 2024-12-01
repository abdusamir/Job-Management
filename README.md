

# **High-Level System Design: Job Management System**

The system is a RESTful web service for managing jobs, enabling CRUD and scheduling operations.

---

## **1. Core Components**

### **1.1. Controllers**
- **`JobController`**
    - Exposes REST endpoints to manage jobs.
    - Functions:
        - Add, start, schedule, check the status of, and delete jobs.
        - Passes data to the service layer.

### **1.2. Service Layer**
- **`JobService`**
    - Contains business logic and orchestrates job operations.
    - Key Responsibilities:
        - Persisting job data.
        - Validating job operations (e.g., whether a job can be deleted).
        - Managing job execution and scheduling.
    - Key Methods:
        - `save(Job job)`: Persist a single job.
        - `saveAll(List<Job> jobs)`: Persist multiple jobs.
        - `runJob(Long id)`: Execute a specific job.
        - `runScheduledJob(Long id, Long delayInSeconds)`: Schedule a job for execution after a delay.
        - `findById(Long id)`: Retrieve job details by ID.
        - `deleteById(Long id)`: Delete a job by ID.
        - `findAll()`: Retrieve all jobs.

### **1.3. Model Layer**
- **`Job`**
    - Represents a job entity with attributes:
        - `id`
        - `status` (enumerated type, e.g., `RUNNING`, `COMPLETED`)
        - `createdAt`

### **1.4. Data Layer**
- **Repository**
    - Interfaces for persisting and retrieving job entities from a database.
    - Likely extends Spring Data JPA repositories.

---

## **2. Interactions Between Components**

1. **Incoming Requests**:
    - Clients interact with the system through HTTP requests sent to `JobController`.

2. **Controller to Service**:
    - The controller forwards requests to the service layer for processing.

3. **Service to Repository**:
    - The service interacts with the data layer to persist, retrieve, or delete job entities.

4. **Response**:
    - The controller returns appropriate responses (e.g., HTTP 200 for success or HTTP 400 for invalid operations).

---

## **3. Key Functionalities**

1. **Add a Job**
    - **Endpoint**: `POST /jobs/addJob`
    - Saves a job after adding a creation timestamp (`createdAt`).

2. **Add Multiple Jobs**
    - **Endpoint**: `POST /jobs/addJobs`
    - Saves multiple jobs in a single operation.

3. **Start a Job**
    - **Endpoint**: `POST /jobs/startJob/{id}`
    - Marks the status of a job as "running."

4. **Schedule a Job**
    - **Endpoint**: `POST /jobs/scheduleJob/{id}`
    - Schedules a job to start after a delay.

5. **Check Job Status**
    - **Endpoint**: `GET /jobs/checkStatus/{id}`
    - Retrieves the current status of a job.

6. **Delete a Job**
    - **Endpoint**: `DELETE /jobs/deleteJob/{id}`
    - Deletes a job unless it is running.

7. **Get All Jobs**
    - **Endpoint**: `GET /jobs/getAllJobs`
    - Retrieves all jobs from the database.

---

## **4. System Architecture**



### **Backend**
- RESTful API implemented using Spring Boot.
- Handles job logic and persistence.

### **Database**
- **Relational Database** (e.g., H2 in memory database).
- **Schema**:
    - `jobs` table with fields like `id`, `createdAt`, and `status`.


### **Deployment**
- Deployed on platforms like OpenShift, Kubernetes, or standalone servers.
- Uses CI/CD tools like Jenkins for automated deployments.

---

# **Instructions to Build and Run the Job Management System Locally with Docker Compose**

This guide provides clear steps to build, configure, and run the Job Management System locally using Docker Compose.

---

## **Prerequisites**
1. **Install Docker and Docker Compose**
    - [Docker Installation Guide](https://docs.docker.com/get-docker/)
    - [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
2. **Verify Installation**
    - Run the following commands:
      ```bash
      docker --version
      docker-compose --version
      ```

3. **Clone the Project**
    - Clone the repository to your local machine:
      ```bash
      git clone <repository-url>
      cd <repository-folder>
      ```

---


## **Step 1: Run the System**
1. **Start Docker Compose**
    - Run the following command in the project root directory:
      ```bash
      docker-compose up --build
      ```

2. **Verify the Services**
    - Ensure both services (`app`) start without errors.

3. **Access the Application**
    - The application will be available at [http://localhost:8080](http://localhost:8080).

---

## **Step 2: Testing the Endpoints**
Use tools like [Postman](https://www.postman.com/) or `curl` to test the endpoints:

- **Add a Job**:
  ```bash
  curl -X POST -H "Content-Type: application/json" -d '{"name":"Example Job","description":"Example description"}' http://localhost:8080/api/v1/jobs/addJob
---
# **Assumptions, Shortcuts, and Technical Debt**

---

## **Assumptions**

1. **Job Scheduler Integration**
    - Assumption: The `runScheduledJob` method relies on a basic scheduling mechanism provided by Spring, but no actual scheduler is implemented.
    - Reasoning: The method only hints at a delay-based execution without showing integration with advanced scheduling libraries like Quartz.
    - Future Work: Integrate a proper scheduler to handle recurring and complex job schedules.

2. **Single Environment Configuration**
    - Assumption: The `application.properties` file uses a single environment (local) configuration for database connections.
    - Reasoning: The code does not indicate separate configurations for development, testing, and production.
    - Future Work: Use profiles (`application-dev.properties`, `application-prod.properties`) for environment-specific configurations.

3. **Happy Path Validation**
    - Assumption: Error handling and validation are minimal (e.g., assuming valid inputs and operational jobs).
    - Reasoning: Simplified for development speed.
    - Future Work: Add detailed validation, exception handling, and error reporting.

4. **In-Memory Scheduler for Jobs**
    - Assumption: Jobs are executed in memory with no external job queue or distributed system.
    - Reasoning: Simplifies implementation and avoids introducing infrastructure dependencies.
    - Future Work: Introduce a distributed task scheduler (e.g., Kafka or Redis queues) to handle job execution reliably.

---

## **Shortcuts Made**

1. **Minimal Logging**
    - Only basic operations are assumed to log outputs (e.g., no detailed logs for errors or job progress).
    - Reasoning: Prioritized core functionality over observability.
    - Future Work: Use a logging framework (e.g., Logback) to add structured and level-based logging.

2. **Lack of Security Measures**
    - No authentication or authorization is implemented for API endpoints.
    - Reasoning: Focused on the core functionality for simplicity.
    - Future Work: Add Spring Security with JWT tokens or OAuth for securing endpoints.

3. **Hardcoded Configuration**
    - Database credentials (`h2` for both username and password)  are hardcoded.
    - Reasoning: Simplified local development setup.
    - Future Work: Use environment variables or a secrets manager to manage sensitive information.

4. **Limited Error Handling**
    - API responses assume all operations succeed without robust exception handling.
    - Reasoning: Avoid complexity during initial development.
    - Future Work: Implement custom exceptions and global exception handling using Spring's `@ControllerAdvice`.

5. **Minimal Unit and Integration Tests**
    - No automated tests are provided for controllers, services, or data access layers.
    - Reasoning: Focused on functional implementation over testing.
    - Future Work: Add unit tests (JUnit, Mockito) and integration tests for all layers.

---

## **Technical Debt Generated**

### 1. **Database Schema Versioning**
- **Debt**: Lack of schema versioning introduces risks during schema evolution.
- **Solution**: Use tools like Flyway or Liquibase to manage database migrations.

### 2. **Scheduler Limitations**
- **Debt**: The simplistic scheduling mechanism cannot handle complex or recurring jobs.
- **Solution**: Integrate with a job scheduler like Quartz or Spring Batch.

### 3. **Insecure API**
- **Debt**: Unsecured endpoints expose the system to unauthorized access.
- **Solution**: Add Spring Security for role-based access control and implement HTTPS.

### 4. **Error Handling and Validation**
- **Debt**: Minimal error handling leads to poor user experience and potential application crashes.
- **Solution**: Implement global exception handling, detailed error messages, and input validation.

### 5. **Testing**
- **Debt**: Lack of tests increases the risk of introducing regressions during updates.
- **Solution**: Write comprehensive unit and integration tests; set up CI pipelines for automated testing.

### 6. **Observability**
- **Debt**: Minimal logging and lack of monitoring tools make debugging and performance tracking difficult.
- **Solution**: Add structured logging, integrate with monitoring tools like Prometheus and Grafana, and enable distributed tracing (e.g., OpenTelemetry).

### 7. **Hardcoded Configuration**
- **Debt**: Hardcoding credentials and configuration limits flexibility and introduces security risks.
- **Solution**: Use `.env` files, environment variables, or a secrets manager.

---

# **Deployment Options for Production Environment**

To deploy the Job Management System in production, it is essential to consider scalability, reliability, security, and maintainability. Below are the recommended deployment options.

---

## **1. Deployment Options**

### **1.1. Containerized Deployment with Kubernetes**
- **Why?**
    - Kubernetes (K8s) offers excellent orchestration for containerized applications, providing scalability, load balancing, and fault tolerance.
- **Steps:**
    1. Use Docker to containerize the application and database.
    2. Create Kubernetes manifests (e.g., `Deployment`, `Service`, `Ingress`) for both the application and database.
    3. Deploy on a managed Kubernetes service such as:
        - **AWS**: Amazon Elastic Kubernetes Service (EKS)
        - **Azure**: Azure Kubernetes Service (AKS)
        - **Google**: Google Kubernetes Engine (GKE)
    4. Use **Helm** charts to manage deployments.

- **Pros:**
    - Scales horizontally based on traffic.
    - Built-in monitoring and recovery (e.g., auto-restart of failed pods).
    - Service discovery and load balancing.
- **Cons:**
    - Higher complexity in setup and management.

---

### **1.2. Cloud Platform as a Service (PaaS)**
- **Why?**
    - Simplifies deployment by abstracting infrastructure details.
- **Options:**
    1. **AWS Elastic Beanstalk**
        - Handles application deployment and scaling with minimal configuration.
    2. **Azure App Service**
        - Integrates seamlessly with Azure's ecosystem.
    3. **Google App Engine**
        - Ideal for rapid deployment with auto-scaling.
    4. **OpenShift**
        - Red Hat’s Kubernetes-based PaaS, offering more control and enterprise features.

- **Pros:**
    - Fast and straightforward deployment.
    - Built-in monitoring, scaling, and logging.
- **Cons:**
    - Limited control over infrastructure.

---

### **1.3. Serverless Deployment**
- **Why?**
    - Ideal for applications with intermittent or unpredictable traffic patterns.
- **Options:**
    1. **AWS Lambda** (with API Gateway)
    2. **Azure Functions**
    3. **Google Cloud Functions**
- **Steps:**
    1. Refactor the application to break down operations into smaller functions.
    2. Deploy the application as serverless functions.

- **Pros:**
    - Auto-scaling based on demand.
    - Cost-efficient (pay-per-use).
- **Cons:**
    - Requires significant refactoring to work within the constraints of serverless architecture.

---

## **2. Supporting Infrastructure for Production**

### **2.1. Database**
- Use a managed database service to offload maintenance:
    - **AWS RDS** (PostgreSQL)
    - **Azure Database for PostgreSQL**
    - **Google Cloud SQL**

### **2.2. Monitoring and Logging**
- **Tools**:
    - **Prometheus & Grafana**: Metrics monitoring and visualization.
    - **ELK Stack (Elasticsearch, Logstash, Kibana)**: Log aggregation and analysis.
    - **Datadog** or **New Relic**: Full-stack application performance monitoring.

### **2.3. Load Balancing**
- Use managed load balancers:
    - **AWS Application Load Balancer**
    - **Azure Load Balancer**
    - **Google Cloud Load Balancing**

### **2.4. Security**
- Secure endpoints using HTTPS with **Let's Encrypt** or managed SSL certificates.
- Use tools like **AWS Secrets Manager** or **HashiCorp Vault** for secure handling of credentials.

### **2.5. CI/CD Pipeline**
- Automate deployments using CI/CD tools:
    - **GitHub Actions**
    - **GitLab CI/CD**
    - **Jenkins**

---

# Swagger
## A swagger compliant API documentation is provided when the app is running
## You can find the swagger at [http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html)
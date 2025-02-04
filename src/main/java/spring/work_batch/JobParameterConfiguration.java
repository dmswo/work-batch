package spring.work_batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {
    @Bean
    public Job BatchJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("Job", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .next(step2(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                    jobParameters.getString("name");
                    jobParameters.getLong("seq");
                    jobParameters.getDate("date");
                    jobParameters.getDouble("age");

                    Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();
                    jobParameters1.get("name");
                    jobParameters1.get("seq");
                    jobParameters1.get("date");
                    jobParameters1.get("age");

                    System.out.println("helloStep1 executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("helloStep2 executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }
}

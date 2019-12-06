package link.myrecipes.batch.job;

import link.myrecipes.batch.service.S3DeleteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Configuration
@Slf4j
public class S3DeleteJobConfig {
    private static final String JOB_NAME = "S3Delete";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String TASKLET_NAME = JOB_NAME + "Tasklet";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final S3DeleteService s3DeleteService;

    public S3DeleteJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, S3DeleteService s3DeleteService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.s3DeleteService = s3DeleteService;
    }

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory
                .get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    public Step step() {
        return stepBuilderFactory
                .get(STEP_NAME)
                .tasklet(tasklet(null))
                .build();
    }

    @Bean(TASKLET_NAME)
    @StepScope
    public Tasklet tasklet(@Value("#{jobParameters['requestDate']}") String requestDate) {
        log.info(requestDate);
        return (contribution, chunkContext) -> {
            List<S3Object> s3ObjectList = this.s3DeleteService.listObjects("recipe");
            s3ObjectList.forEach(s3Object -> log.info(s3Object.key()));
            return RepeatStatus.FINISHED;
        };
    }
}

package link.myrecipes.batch.job;

import link.myrecipes.batch.service.DeleteStepImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static link.myrecipes.batch.job.DeleteStepImageJobConfig.JOB_NAME;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
@Slf4j
public class DeleteStepImageJobConfig {
    public static final String JOB_NAME = "DeleteStepImage";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String TASKLET_NAME = JOB_NAME + "Tasklet";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DeleteStepImageService deleteStepImageService;

    public DeleteStepImageJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DeleteStepImageService deleteStepImageService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.deleteStepImageService = deleteStepImageService;
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
    public Tasklet tasklet(@Value("#{jobParameters['version']}") String version) {
        return (contribution, chunkContext) -> {
            if (version != null) {
                this.deleteStepImageService.delete("step");
            }
            return RepeatStatus.FINISHED;
        };
    }
}

package link.myrecipes.batch.job;

import link.myrecipes.batch.service.MakePopularRecipes;
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

import static link.myrecipes.batch.job.MakePopularRecipesJobConfig.JOB_NAME;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
@Slf4j
public class MakePopularRecipesJobConfig {
    public static final String JOB_NAME = "MakePopularRecipes";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String TASKLET_NAME = JOB_NAME + "Tasklet";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MakePopularRecipes makePopularRecipes;

    public MakePopularRecipesJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                       MakePopularRecipes makePopularRecipes) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.makePopularRecipes = makePopularRecipes;
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
                this.makePopularRecipes.make();
            }
            return RepeatStatus.FINISHED;
        };
    }
}

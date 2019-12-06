package link.myrecipes.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableBatchProcessing
public class MyrecipesBatchApplication {
    private static final String PROPERTIES_LOCATIONS = "spring.config.location="
            + "classpath:/application.yml,"
            + "classpath:/aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(MyrecipesBatchApplication.class)
                .properties(PROPERTIES_LOCATIONS)
                .run(args);
    }

}

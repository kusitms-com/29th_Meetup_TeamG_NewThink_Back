package kusitms.duduk;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = {
    "kusitms.duduk"
})
public class DudukBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DudukBatchApplication.class, args);
    }
}
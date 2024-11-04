package ovh.eukon05.infodb.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "infodb", description = "A REST API exposing articles regularly collected from multiple news sources", version = "v1"))
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

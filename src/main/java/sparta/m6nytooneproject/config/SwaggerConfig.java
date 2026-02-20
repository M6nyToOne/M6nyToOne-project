package sparta.m6nytooneproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("M6nyToOne 팀프로젝트 API")
                        .description("M6nyToOne API 문서")
                        .version("v1.0.0"));
    }

}

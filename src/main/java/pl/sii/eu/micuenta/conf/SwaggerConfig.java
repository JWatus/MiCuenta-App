package pl.sii.eu.micuenta.conf;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo metaInfo() {
        return new ApiInfoBuilder()
                .title("MiCuenta API")
                .description("MiCuenta app API documentation for handling debts")
                .version("1.0")
                .license("Apache License version 2.0")
                .licenseUrl("https://www.apache.org/licenses")
                .contact(new Contact("Sii", "https://sii.pl", "mbodzek@pl.sii.eu;jwatus@pl.sii.eu"))
                .build();
    }
}

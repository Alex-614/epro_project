package de.thbingen.epro.proect.okrgateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIGatewayConfig {

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                return builder.routes() // keep the order, it is significant
                                // OKR-Service route
                                .route("okr-service", r -> r.path("/api/**")
                                                .uri("http://okr-service:8081"))
                                // angular-application route
                                .route("angular-application", r -> r.path("/**")
                                                .uri("http://angular-application:80"))
                                .build();
        }
        
}

package homework.apigateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class GatewayConfig {
    private final CircuitBreaker userServiceCircuitBreaker;
    private final CircuitBreaker notificationServiceCircuitBreaker;

    public GatewayConfig(
            @Qualifier("userServiceCircuitBreaker") CircuitBreaker userServiceCircuitBreaker,
            @Qualifier("notificationServiceCircuitBreaker") CircuitBreaker notificationServiceCircuitBreaker) {
        this.userServiceCircuitBreaker = userServiceCircuitBreaker;
        this.notificationServiceCircuitBreaker = notificationServiceCircuitBreaker;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-route", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user")))
                        .uri("lb://service-user"))
                .route("notification-service-route", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("notificationServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notification")))
                        .uri("lb://service-notification"))
                .build();
    }
}

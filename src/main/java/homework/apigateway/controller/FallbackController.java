package homework.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/user")
    public String userServiceFallback() {
        return "User Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/fallback/notification")
    public String notificationServiceFallback() {
        return "Notification Service is currently unavailable. Please try again later.";
    }
}

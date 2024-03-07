package org.gabrielbarrilli.securitytelegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SecurityTelegramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityTelegramApplication.class, args);
    }

}

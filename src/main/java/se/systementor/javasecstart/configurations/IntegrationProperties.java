package se.systementor.javasecstart.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "integrations")
@Getter
@Setter
public class IntegrationProperties {
    private MailConfig mailConfig;

    @Getter
    @Setter
    public static class MailConfig {
        private String host;
        private int port;
        private String username;
        private String password;
    }
}

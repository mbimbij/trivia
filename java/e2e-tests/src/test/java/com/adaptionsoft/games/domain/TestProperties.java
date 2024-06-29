package com.adaptionsoft.games.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test")
@Data
public class TestProperties {
    private String qaUserId;
    private String qaUserPassword;
    private String frontendUrlBase;
    private String backendUrlBase;
}

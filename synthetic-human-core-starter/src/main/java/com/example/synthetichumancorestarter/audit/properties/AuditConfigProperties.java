package com.example.synthetichumancorestarter.audit.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("synthetichumancorestarter.audit")
@Data
public class AuditConfigProperties {

    public AuditMode mode = AuditMode.CONSOLE;
    public String topic;

    public enum AuditMode {
        KAFKA, CONSOLE
    }
}
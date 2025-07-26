package com.example.synthetichumancorestarter.audit.service.kafka;

import com.example.synthetichumancorestarter.audit.model.AuditEvent;
import com.example.synthetichumancorestarter.audit.properties.AuditConfigProperties;
import com.example.synthetichumancorestarter.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "synth.core.audit.mode", havingValue = "KAFKA")
@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    private final AuditConfigProperties auditConfigProperties;

    @Override
    public void audit(AuditEvent event) {
        kafkaTemplate.send(auditConfigProperties.getTopic(), event);
    }
}

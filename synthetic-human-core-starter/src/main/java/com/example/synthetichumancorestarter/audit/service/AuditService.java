package com.example.synthetichumancorestarter.audit.service;

import com.example.synthetichumancorestarter.audit.model.AuditEvent;

public interface AuditService {
    void audit(AuditEvent event);
}

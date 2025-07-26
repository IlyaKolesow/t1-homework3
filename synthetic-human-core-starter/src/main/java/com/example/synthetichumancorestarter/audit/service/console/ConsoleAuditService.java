package com.example.synthetichumancorestarter.audit.service.console;

import com.example.synthetichumancorestarter.audit.model.AuditEvent;
import com.example.synthetichumancorestarter.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "synthetichumancorestarter.audit.mode", havingValue = "CONSOLE", matchIfMissing = true)
public class ConsoleAuditService implements AuditService {

    @Override
    public void audit(AuditEvent event) {
        log.info("""
                        Audit event:
                        \tTime - {}
                        \tMethod- {}
                        \tParameters - {}
                        \tState - {}""",
                event.getTime(),
                event.getMethod(),
                event.getParams(),
                event.getState());
    }

}
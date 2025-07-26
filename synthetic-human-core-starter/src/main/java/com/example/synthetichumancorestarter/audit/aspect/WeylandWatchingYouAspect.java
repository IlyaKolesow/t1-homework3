package com.example.synthetichumancorestarter.audit.aspect;

import com.example.synthetichumancorestarter.audit.model.AuditEvent;
import com.example.synthetichumancorestarter.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class WeylandWatchingYouAspect {

    private final AuditService auditService;

    @Around("@annotation(com.example.synthetichumancorestarter.audit.aspect.WeylandWatchingYou)")
    public Object proceedMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final var auditEventBuilder = AuditEvent.builder();
        final var method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        auditEventBuilder.time(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        auditEventBuilder.method(method.getName());
        auditEventBuilder.params(extractMethodParameters(method, proceedingJoinPoint.getArgs()));
        try {
            final var result = proceedingJoinPoint.proceed();
            auditEventBuilder.state(AuditEvent.MethodExecutionState.SUCCESS);
            return result;
        } catch (Throwable e) {
            auditEventBuilder.state(AuditEvent.MethodExecutionState.EXCEPTION);
            throw e;
        } finally {
            auditService.audit(auditEventBuilder.build());
        }
    }

    public Map<String, AuditEvent.Param> extractMethodParameters(Method method, Object[] args) {
        final var params = new HashMap<String, AuditEvent.Param>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            params.put(param.getName(), new AuditEvent.Param(param.getType().getSimpleName(), args[i]));
        }
        return params;
    }

}
package com.example.synthetichumancorestarter.command.service;

import com.example.synthetichumancorestarter.command.model.SynthCommand;
import com.example.synthetichumancorestarter.command.service.exception.ExecutionQueueIsFullException;

public interface CommandService {
    void processCommand(SynthCommand command) throws ExecutionQueueIsFullException;
}

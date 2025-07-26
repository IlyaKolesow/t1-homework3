package com.example.bishopprototype.api;

import com.example.bishopprototype.api.model.CommandType;
import com.example.bishopprototype.api.model.Initiator;
import com.example.bishopprototype.command.BishopCommandService;
import com.example.bishopprototype.command.exception.UnavailableCommandException;
import com.example.synthetichumancorestarter.command.service.exception.ExecutionQueueIsFullException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
@RequiredArgsConstructor
public class BishopApiV1 {
    private final BishopCommandService commandService;

    @PostMapping
    public void processCommand(
            @RequestParam CommandType commandType,
            @RequestParam Initiator initiator) throws UnavailableCommandException, ExecutionQueueIsFullException {
        commandService.runCommand(commandType, initiator);
    }
}
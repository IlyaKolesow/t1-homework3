package com.example.bishopprototype.command;

import com.example.bishopprototype.api.model.CommandType;
import com.example.bishopprototype.api.model.Initiator;
import com.example.bishopprototype.command.exception.UnavailableCommandException;
import com.example.synthetichumancorestarter.audit.aspect.WeylandWatchingYou;
import com.example.synthetichumancorestarter.command.model.SynthCommand;
import com.example.synthetichumancorestarter.command.model.SynthCommandPriority;
import com.example.synthetichumancorestarter.command.service.CommandService;
import com.example.synthetichumancorestarter.command.service.exception.ExecutionQueueIsFullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BishopCommandService {

    private final CommandService commandService;

    @WeylandWatchingYou
    public void runCommand(CommandType commandType, Initiator initiator) throws UnavailableCommandException, ExecutionQueueIsFullException {
        final var synthCommandBuilder = SynthCommand.builder();
        synthCommandBuilder.description(chooseDescription(commandType));
        synthCommandBuilder.author(initiator.name());
        synthCommandBuilder.priority(choosePriority(initiator));
        synthCommandBuilder.time(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        commandService.processCommand(synthCommandBuilder.build());
    }

    private String chooseDescription(CommandType commandType) throws UnavailableCommandException {
        return switch (commandType) {
            case ALERT -> "ALERT! ALERT! ALERT!";
            case HELP -> "Try to help...";
            case KILL -> throw new UnavailableCommandException("I cannot killed yet... May be another time...");
            case ENGINE -> "Processing engine... Try to fix...";
        };
    }

    private SynthCommandPriority choosePriority(Initiator initiator) {
        return switch (initiator) {
            case REGULAR_HUMAN -> SynthCommandPriority.COMMON;
            case WEYLAND_YUTANI_OFFICER -> SynthCommandPriority.CRITICAL;
        };
    }

}
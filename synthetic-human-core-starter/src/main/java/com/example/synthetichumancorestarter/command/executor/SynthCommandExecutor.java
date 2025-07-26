package com.example.synthetichumancorestarter.command.executor;

import com.example.synthetichumancorestarter.command.model.SynthCommand;

public interface SynthCommandExecutor {
    void execute(SynthCommand synthCommand);
}

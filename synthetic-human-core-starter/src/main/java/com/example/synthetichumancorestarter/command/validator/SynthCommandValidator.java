package com.example.synthetichumancorestarter.command.validator;

import com.example.synthetichumancorestarter.command.model.SynthCommand;

public interface SynthCommandValidator {
    void validate(SynthCommand command);
}
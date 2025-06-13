package org.api.restObjects.manualcyclometer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.api.restObjects.enigma.Enigma;

public record ManualCyclometerRequest(
        @Valid
        @NotNull(message = "Missing required 'enigma' object.")
        Enigma enigma,

        @Valid
        @NotNull(message = "Missing required 'parameters' object.")
        ManualCyclometerParameters parameters) {}

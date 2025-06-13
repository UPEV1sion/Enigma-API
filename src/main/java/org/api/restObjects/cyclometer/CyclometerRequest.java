package org.api.restObjects.cyclometer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.api.restObjects.enigma.Enigma;

public record CyclometerRequest(
        @Valid
        @NotNull(message = "Missing required 'enigma' object.")
        Enigma enigma,
        @Valid
        @NotNull(message = "Missing required 'parameters' object.")
        CyclometerParameters parameters) {}

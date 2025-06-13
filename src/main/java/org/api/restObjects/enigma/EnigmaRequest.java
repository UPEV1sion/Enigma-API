package org.api.restObjects.enigma;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EnigmaRequest(
        @Valid
        @NotNull(message = "Missing required 'enigma' object.")
        Enigma enigma) {}

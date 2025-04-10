package org.api.restObjects.enigma;

import jakarta.validation.Valid;

public record EnigmaRequest(
        @Valid
        Enigma enigma) {}

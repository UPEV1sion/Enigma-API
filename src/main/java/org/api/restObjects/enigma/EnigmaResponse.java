package org.api.restObjects.enigma;

import jakarta.validation.Valid;

public record EnigmaResponse (
        @Valid
        String output){}

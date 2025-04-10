package org.api.restObjects.cyclometer;

import jakarta.validation.Valid;
import org.api.restObjects.enigma.Enigma;

public record CyclometerRequest(@Valid Enigma enigma, @Valid CyclometerParameters parameters) {}

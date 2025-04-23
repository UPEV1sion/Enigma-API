package org.api.restObjects.manualcyclometer;

import jakarta.validation.Valid;
import org.api.restObjects.enigma.Enigma;

public record ManualCyclometerRequest(@Valid Enigma enigma, @Valid ManualCyclometerParameters parameters) {}

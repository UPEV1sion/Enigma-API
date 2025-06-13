package org.api.restObjects.catalogue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.api.restObjects.cyclometer.CyclometerCycles;

public record CatalogueRequest(
        @Valid
        @NotNull(message = "Missing required 'cycles' object.")
        CyclometerCycles cycles,
        @Valid
        @NotNull(message = "Missing required 'parameters' object.")
        CatalogueParameters parameters) {
}

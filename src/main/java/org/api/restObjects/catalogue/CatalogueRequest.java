package org.api.restObjects.catalogue;

import jakarta.validation.Valid;
import org.api.restObjects.cyclometer.CyclometerCycles;

public record CatalogueRequest(
        @Valid
        CyclometerCycles cycles,
        @Valid
        CatalogueParameters parameters) {
}

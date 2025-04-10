package org.api.restObjects.catalogue;

import org.api.restObjects.validators.catalogue.annotations.ValidCataloguePage;

public record CatalogueParameters(
        @ValidCataloguePage
        int page) {
}

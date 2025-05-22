package org.api.restObjects.catalogue;

import org.api.restObjects.validators.catalogue.annotations.*;


public record CatalogueParameters(
        @ValidCataloguePage
        Integer page,
        @ValidCatalogueSortBy
        String sortBy,
        @ValidCatalogueSortDir
        String sortDir,
        @ValidCatalogueRotorOrder
        Integer[] rotorOrder,
        @ValidCatalogueRotorPosition
        Integer[] rotorPosition
) {
        public CatalogueParameters {
                if (rotorOrder == null) {
                        rotorOrder = new Integer[0]; // Default value for rotorOrder
                }
                if (rotorPosition == null) {
                        rotorPosition = new Integer[0]; // Default value for rotorPosition
                }
        }
}

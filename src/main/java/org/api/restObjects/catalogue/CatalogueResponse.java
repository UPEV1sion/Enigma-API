package org.api.restObjects.catalogue;

import java.util.List;

public record CatalogueResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages) {
}

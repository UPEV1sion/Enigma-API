package org.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

public class RotorCharacteristicCacheKey {
    private final Integer[]firstCycleCounts;
    private final Integer[] secondCycleCounts;
    private final Integer[] thirdCycleCounts;
    private final Integer[]  rotorOrder;
    private final Integer[]  rotorPosition;
    private final int page;
    private final int size;
    private final String sortProperty;
    private final String sortDirection;

    public RotorCharacteristicCacheKey(
            Integer[] firstCycleCounts,
            Integer[] secondCycleCounts,
            Integer[] thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable) {

        this.firstCycleCounts = firstCycleCounts;
        this.secondCycleCounts = secondCycleCounts;
        this.thirdCycleCounts = thirdCycleCounts;
        this.rotorOrder = rotorOrder;
        this.rotorPosition = rotorPosition;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();

        if (pageable.getSort().iterator().hasNext()) {
            Sort.Order order = pageable.getSort().iterator().next();
            this.sortProperty = order.getProperty();
            this.sortDirection = order.getDirection().name();
        } else {
            this.sortProperty = null;
            this.sortDirection = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RotorCharacteristicCacheKey that)) return false;
        return page == that.page &&
                size == that.size &&
                Arrays.equals(firstCycleCounts, that.firstCycleCounts) &&
                Arrays.equals(secondCycleCounts, that.secondCycleCounts) &&
                Arrays.equals(thirdCycleCounts, that.thirdCycleCounts) &&
                Arrays.equals(rotorOrder, that.rotorOrder) &&
                Arrays.equals(rotorPosition, that.rotorPosition) &&
                Objects.equals(sortProperty, that.sortProperty) &&
                Objects.equals(sortDirection, that.sortDirection);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(firstCycleCounts);
        result = 31 * result + Arrays.hashCode(secondCycleCounts);
        result = 31 * result + Arrays.hashCode(thirdCycleCounts);
        result = 31 * result + Arrays.hashCode(rotorOrder);
        result = 31 * result + Arrays.hashCode(rotorPosition);
        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (sortProperty != null ? sortProperty.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        return result;
    }
}

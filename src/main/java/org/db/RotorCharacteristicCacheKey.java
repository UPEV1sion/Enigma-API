package org.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RotorCharacteristicCacheKey {
    private final Map<Integer,Integer> firstCycleCounts;
    private final Map<Integer,Integer> secondCycleCounts;
    private final Map<Integer,Integer> thirdCycleCounts;
    private final List<Integer> rotorOrder;
    private final List<Integer> rotorPosition;
    private final int page;
    private final int size;
    private final String sortProperty;
    private final String sortDirection;

    public RotorCharacteristicCacheKey(
            Map<Integer,Integer> firstCycleCounts,
            Map<Integer,Integer> secondCycleCounts,
            Map<Integer,Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable) {

        this.firstCycleCounts = firstCycleCounts;
        this.secondCycleCounts = secondCycleCounts;
        this.thirdCycleCounts = thirdCycleCounts;
        this.rotorOrder = rotorOrder == null ? List.of() : List.of(rotorOrder);
        this.rotorPosition = rotorPosition == null ? List.of() : List.of(rotorPosition);
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
                Objects.equals(firstCycleCounts, that.firstCycleCounts) &&
                Objects.equals(secondCycleCounts, that.secondCycleCounts) &&
                Objects.equals(thirdCycleCounts, that.thirdCycleCounts) &&
                Objects.equals(rotorOrder, that.rotorOrder) &&
                Objects.equals(rotorPosition, that.rotorPosition) &&
                Objects.equals(sortProperty, that.sortProperty) &&
                Objects.equals(sortDirection, that.sortDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCycleCounts, secondCycleCounts, thirdCycleCounts, rotorOrder, rotorPosition, page, size, sortProperty, sortDirection);
    }
}

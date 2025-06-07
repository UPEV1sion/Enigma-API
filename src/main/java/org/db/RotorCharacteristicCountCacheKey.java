package org.db;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RotorCharacteristicCountCacheKey {
    private final Map<Integer,Integer> firstCycleCounts;
    private final Map<Integer,Integer> secondCycleCounts;
    private final Map<Integer,Integer> thirdCycleCounts;
    private final List<Integer> rotorOrder;
    private final List<Integer> rotorPosition;

    public RotorCharacteristicCountCacheKey(
            Map<Integer,Integer> firstCycleCounts,
            Map<Integer,Integer> secondCycleCounts,
            Map<Integer,Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition
    ) {
        this.firstCycleCounts = firstCycleCounts;
        this.secondCycleCounts = secondCycleCounts;
        this.thirdCycleCounts = thirdCycleCounts;
        this.rotorOrder = rotorOrder == null ? List.of() : List.of(rotorOrder);
        this.rotorPosition = rotorPosition == null ? List.of() : List.of(rotorPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RotorCharacteristicCountCacheKey that)) return false;
        return Objects.equals(firstCycleCounts, that.firstCycleCounts) &&
                Objects.equals(secondCycleCounts, that.secondCycleCounts) &&
                Objects.equals(thirdCycleCounts, that.thirdCycleCounts) &&
                Objects.equals(rotorOrder, that.rotorOrder) &&
                Objects.equals(rotorPosition, that.rotorPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCycleCounts, secondCycleCounts, thirdCycleCounts, rotorOrder, rotorPosition);
    }
}
package org.db;

import java.util.Arrays;

public class RotorCharacteristicCountCacheKey {
    private final Integer[] firstCycleCounts;
    private final Integer[] secondCycleCounts;
    private final Integer[] thirdCycleCounts;
    private final Integer[] rotorOrder;
    private final Integer[] rotorPosition;

    public RotorCharacteristicCountCacheKey(
            Integer[] firstCycleCounts,
            Integer[] secondCycleCounts,
            Integer[] thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition
    ) {
        this.firstCycleCounts = firstCycleCounts;
        this.secondCycleCounts = secondCycleCounts;
        this.thirdCycleCounts = thirdCycleCounts;
        this.rotorOrder = rotorOrder;
        this.rotorPosition = rotorPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RotorCharacteristicCountCacheKey that)) return false;
        return Arrays.equals(firstCycleCounts, that.firstCycleCounts) &&
                Arrays.equals(secondCycleCounts, that.secondCycleCounts) &&
                Arrays.equals(thirdCycleCounts, that.thirdCycleCounts) &&
                Arrays.equals(rotorOrder, that.rotorOrder) &&
                Arrays.equals(rotorPosition, that.rotorPosition);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(firstCycleCounts);
        result = 31 * result + Arrays.hashCode(secondCycleCounts);
        result = 31 * result + Arrays.hashCode(thirdCycleCounts);
        result = 31 * result + Arrays.hashCode(rotorOrder);
        result = 31 * result + Arrays.hashCode(rotorPosition);
        return result;
    }
}

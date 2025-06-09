package org.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface RotorCharacteristicRepositoryCustom {
    Page<RotorCharacteristic> findRotorCharacteristic(
            Integer[] firstCycle,
            Integer[] secondCycle,
            Integer[] thirdCycle,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable,
            RotorCharacteristicCacheKey cacheKey,
            Long totalCount
    );

    Long countRotorCharacteristics(
            Integer[] firstCycle,
            Integer[] secondCycle,
            Integer[] thirdCycle,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            RotorCharacteristicCountCacheKey countCacheKey
    );
}
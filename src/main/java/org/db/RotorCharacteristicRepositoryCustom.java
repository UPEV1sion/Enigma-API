package org.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.Map;

public interface RotorCharacteristicRepositoryCustom {
    Page<RotorCharacteristic> findRotorCharacteristic(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable,
            RotorCharacteristicCacheKey cacheKey,
            Long totalCount
    );

    Long countRotorCharacteristics(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            RotorCharacteristicCountCacheKey countCacheKey
    );
}
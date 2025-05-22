package org.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.Map;

public interface RotorCharacteristicRepositoryCustom {
    Page<RotorCharacteristic> findRotorCharacteristicWithCounts(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable,
            RotorCharacteristicCacheKey cacheKey
    );
}
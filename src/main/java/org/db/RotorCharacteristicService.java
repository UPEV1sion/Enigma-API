package org.db;

import org.api.restObjects.catalogue.CatalogueParameters;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.springframework.stereotype.Service;


import java.util.Arrays;

import static org.db.DatabaseConfig.PAGE_SIZE;

@Service
public class RotorCharacteristicService {
    private final RotorCharacteristicRepository rotorCharacteristicRepository;

    public RotorCharacteristicService(RotorCharacteristicRepository rotorCharacteristicRepository) {
        this.rotorCharacteristicRepository = rotorCharacteristicRepository;
    }

    @Transactional(readOnly = true)
    public Page<RotorCharacteristic> searchConfig(
            CyclometerCycles computedCycles, final CatalogueParameters catalogueParameters) {

        Sort sort = Sort.unsorted(); // no sort by default
        if (catalogueParameters.sortBy() != null && !catalogueParameters.sortBy().isBlank()) {
            sort = Sort.by(Sort.Direction.fromOptionalString(catalogueParameters.sortDir()).orElse(Sort.Direction.ASC), catalogueParameters.sortBy());
        }
        Pageable pageable = PageRequest.of(catalogueParameters.page(), PAGE_SIZE, sort);

        Integer[] first = Arrays.stream(computedCycles.firstToThird()).boxed().toArray(Integer[]::new);
        Integer[] second = Arrays.stream(computedCycles.secondToFourth()).boxed().toArray(Integer[]::new);
        Integer[] third = Arrays.stream(computedCycles.thirdToSixth()).boxed().toArray(Integer[]::new);


        RotorCharacteristicCacheKey cacheKey = new RotorCharacteristicCacheKey(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition(),
                pageable
        );

        RotorCharacteristicCountCacheKey countKey = new RotorCharacteristicCountCacheKey(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition()
        );

        Long totalCount = rotorCharacteristicRepository.countRotorCharacteristics(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition(),
                countKey
        );


        return rotorCharacteristicRepository.findRotorCharacteristic(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition(),
                pageable,
                cacheKey,
                totalCount
        );
    }
}

package org.db;

import org.api.restObjects.catalogue.CatalogueParameters;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.db.DatabaseConfig.PAGE_SIZE;

@Service
public class RotorCharacteristicService {
    private final RotorCharacteristicRepository rotorCharacteristicRepository;

    private Map<Integer, Integer> convertToCountMap(int[] values) {
        return Arrays.stream(values)
                .boxed()
                .collect(Collectors.toMap(
                        v -> v,
                        _ -> 1,
                        Integer::sum // falls Duplikate vorkommen
                ));
    }

    public RotorCharacteristicService(RotorCharacteristicRepository rotorCharacteristicRepository) {
        this.rotorCharacteristicRepository = rotorCharacteristicRepository;
    }

    @Transactional(readOnly = true)
    public Page<RotorCharacteristic> searchConfig(
            CyclometerCycles computedCycles, final CatalogueParameters catalogueParameters) {

        Sort sort = Sort.unsorted(); // no sort by default
        if (catalogueParameters.sortBy() != null && !catalogueParameters.sortBy().isBlank()) {
            Sort.Direction direction = Sort.Direction.fromOptionalString(catalogueParameters.sortDir()).orElse(Sort.Direction.ASC);
            sort = Sort.by(direction, catalogueParameters.sortBy());
        }
        Pageable pageable = PageRequest.of(catalogueParameters.page(), PAGE_SIZE, sort);

        Map<Integer, Integer> first = convertToCountMap(computedCycles.firstToThird());
        Map<Integer, Integer> second = convertToCountMap(computedCycles.secondToFourth());
        Map<Integer, Integer> third = convertToCountMap(computedCycles.thirdToSixth());

        RotorCharacteristicCacheKey cacheKey = new RotorCharacteristicCacheKey(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition(),
                pageable
        );

        return rotorCharacteristicRepository.findRotorCharacteristicWithCounts(
                first,
                second,
                third,
                catalogueParameters.rotorOrder(),
                catalogueParameters.rotorPosition(),
                pageable,
                cacheKey
        );
    }
}

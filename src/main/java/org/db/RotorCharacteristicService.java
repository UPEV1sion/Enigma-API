package org.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotorCharacteristicService {
    private final RotorCharacteristicRepository rotorCharacteristicRepository;

    public RotorCharacteristicService(RotorCharacteristicRepository rotorCharacteristicRepository) {
        this.rotorCharacteristicRepository = rotorCharacteristicRepository;
    }

    @Transactional(readOnly = true)
    public Page<RotorCharacteristic> searchConfig(CyclometerCycles computedCycles, final int page) {
        Pageable pageable = PageRequest.of(page, DatabaseConfig.PAGE_SIZE);
        List<RotorCharacteristic> results = rotorCharacteristicRepository.findRotorCharacteristic(
                computedCycles.firstToThird(),
                computedCycles.secondToFourth(),
                computedCycles.thirdToSixth(),
                pageable);

        return new PageImpl<>(results, pageable, DatabaseConfig.CATALOGUE_SIZE);
    }
}

package org.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RotorCharacteristicRepository extends JpaRepository<RotorCharacteristic, Long>,
        RotorCharacteristicRepositoryCustom {}
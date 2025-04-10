package org.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotorCharacteristicRepository extends JpaRepository<RotorCharacteristic, Long> {

    @Query(value = """
            SELECT * FROM cycles
            WHERE "one_to_four_permut" @> :firstCycle
              AND "two_to_five_permut" @> :secondCycle
              AND "three_to_six_permut" @> :thirdCycle
            """,
            nativeQuery = true)
    List<RotorCharacteristic> findRotorCharacteristic(
            @Param("firstCycle") int[] firstCycle,
            @Param("secondCycle") int[] secondCycle,
            @Param("thirdCycle") int[] thirdCycle,
            Pageable pageable
    );
}

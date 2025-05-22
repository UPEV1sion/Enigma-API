package org.db;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RotorCharacteristicRepositoryImpl implements RotorCharacteristicRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "rotorCharacteristics", key = "#cacheKey")
    public Page<RotorCharacteristic> findRotorCharacteristicWithCounts(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable,
            RotorCharacteristicCacheKey cacheKey
    ) {
        Sort sort = pageable.getSort();
        String orderByClause = "";

        if (!sort.isUnsorted()) {
            orderByClause = " ORDER BY " + sort.stream()
                    .map(order -> "c." + order.getProperty() + " " + order.getDirection().name())
                    .collect(Collectors.joining(", "));

        }


        Integer[] firstCycleArray = firstCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] secondCycleArray = secondCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] thirdCycleArray = thirdCycleCounts.keySet().toArray(new Integer[0]);

        rotorOrder = (rotorOrder == null || rotorOrder.length == 0) ? new Integer[0] : rotorOrder;
        rotorPosition = (rotorPosition == null || rotorPosition.length == 0) ? new Integer[0] : rotorPosition;

        StringBuilder frequencyChecks = new StringBuilder();

        firstCycleCounts.forEach((value, count) -> frequencyChecks.append(
                String.format("""
            AND (SELECT COUNT(*) FROM unnest(p.one_to_four_permut) val WHERE val = %d) >= %d
        """, value, count)));

        secondCycleCounts.forEach((value, count) -> frequencyChecks.append(
                String.format("""
            AND (SELECT COUNT(*) FROM unnest(p.two_to_five_permut) val WHERE val = %d) >= %d
        """, value, count)));

        thirdCycleCounts.forEach((value, count) -> frequencyChecks.append(
                String.format("""
            AND (SELECT COUNT(*) FROM unnest(p.three_to_six_permut) val WHERE val = %d) >= %d
        """, value, count)));

        String frequencyClause = frequencyChecks.length() > 0 ? " " + frequencyChecks.toString() : "";


        String sql = String.format("""
                    WITH prefiltered AS (
                        SELECT *
                        FROM permutations p
                        WHERE (:firstCycleArray IS NULL OR cardinality(:firstCycleArray) = 0 OR p.one_to_four_permut @> cast(:firstCycleArray AS integer[]))
                          AND (:secondCycleArray IS NULL OR cardinality(:secondCycleArray) = 0 OR p.two_to_five_permut @> cast(:secondCycleArray AS integer[]))
                          AND (:thirdCycleArray IS NULL OR cardinality(:thirdCycleArray) = 0 OR p.three_to_six_permut @> cast(:thirdCycleArray AS integer[]))
                        %s
                    )
                    SELECT\s
                        pf.*,\s
                        c.*
                    FROM prefiltered pf
                    JOIN config c ON pf.permut_id = c.permut_id
                    WHERE (:rotorOrder IS NULL OR cardinality(:rotorOrder) = 0 OR c.rotor_order = cast(:rotorOrder AS integer[]))
                      AND (:rotorPosition IS NULL OR cardinality(:rotorPosition) = 0 OR c.rotor_position = cast(:rotorPosition AS integer[]))
                      %s
                    LIMIT :limit OFFSET :offset
               \s""", frequencyClause, orderByClause);

        // Count-Query (für Gesamtzahl)
        String countSql = String.format("""
                    WITH prefiltered AS (
                        SELECT *
                        FROM permutations p
                        WHERE (:firstCycleArray IS NULL OR cardinality(:firstCycleArray) = 0 OR p.one_to_four_permut @> cast(:firstCycleArray AS integer[]))
                          AND (:secondCycleArray IS NULL OR cardinality(:secondCycleArray) = 0 OR p.two_to_five_permut @> cast(:secondCycleArray AS integer[]))
                          AND (:thirdCycleArray IS NULL OR cardinality(:thirdCycleArray) = 0 OR p.three_to_six_permut @> cast(:thirdCycleArray AS integer[]))
                          %s
                    )
                    SELECT COUNT(*)
                    FROM prefiltered pf
                    JOIN config c ON pf.permut_id = c.permut_id
                    WHERE (:rotorOrder IS NULL OR cardinality(:rotorOrder) = 0 OR c.rotor_order = cast(:rotorOrder AS integer[]))
                      AND (:rotorPosition IS NULL OR cardinality(:rotorPosition) = 0 OR c.rotor_position = cast(:rotorPosition AS integer[]))
                """, frequencyClause);

        Session session = entityManager.unwrap(Session.class);

        System.out.println(sql);
        NativeQuery<RotorCharacteristic> query = session.createNativeQuery(sql, RotorCharacteristic.class);
        query.setParameter("firstCycleArray", firstCycleArray);
        query.setParameter("secondCycleArray", secondCycleArray);
        query.setParameter("thirdCycleArray", thirdCycleArray);
        query.setParameter("rotorOrder", rotorOrder);
        query.setParameter("rotorPosition", rotorPosition);
        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());


        List<RotorCharacteristic> results = query.getResultList();

        // Count-Query
        NativeQuery<Long> countQuery = session.createNativeQuery(countSql, Long.class);
        countQuery.setParameter("firstCycleArray", firstCycleArray);
        countQuery.setParameter("secondCycleArray", secondCycleArray);
        countQuery.setParameter("thirdCycleArray", thirdCycleArray);
        countQuery.setParameter("rotorOrder", rotorOrder);
        countQuery.setParameter("rotorPosition", rotorPosition);

        Long totalCount = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, totalCount);
    }
}

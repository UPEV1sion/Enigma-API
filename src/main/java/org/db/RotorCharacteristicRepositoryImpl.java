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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RotorCharacteristicRepositoryImpl implements RotorCharacteristicRepositoryCustom {

    private Map<Integer, Integer> convertToCountMap(int[] values) {
        return Arrays.stream(values)
                .boxed()
                .collect(Collectors.toMap(
                        v -> v,
                        v -> 1,
                        Integer::sum // falls Duplikate vorkommen
                ));
    }

    private String buildDynamicWhereClause(Integer[] firstCycleArray, Integer[] secondCycleArray, Integer[] thirdCycleArray) {
        List<String> whereConditions = new ArrayList<>();

        if (firstCycleArray != null && firstCycleArray.length > 0) {
            whereConditions.add("p.one_to_four_permut @> cast(:firstCycleArray AS integer[])");
        }
        if (secondCycleArray != null && secondCycleArray.length > 0) {
            whereConditions.add("p.two_to_five_permut @> cast(:secondCycleArray AS integer[])");
        }
        if (thirdCycleArray != null && thirdCycleArray.length > 0) {
            whereConditions.add("p.three_to_six_permut @> cast(:thirdCycleArray AS integer[])");
        }

        if (whereConditions.isEmpty()) {
            return "";
        }

        return "WHERE " + String.join("\n  AND ", whereConditions);
    }


    @Autowired
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "rotorCharacteristics", key = "#cacheKey")
    public Page<RotorCharacteristic> findRotorCharacteristic(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            Pageable pageable,
            RotorCharacteristicCacheKey cacheKey,
            Long totalCount
    ) {
        Sort sort = pageable.getSort();
        String orderByClause = "";

        if (!sort.isUnsorted()) {
            orderByClause = " ORDER BY " + sort.stream()
                    .map(order -> {
                        String prefix = order.getProperty().startsWith("one_to_four_permut")
                                || order.getProperty().startsWith("two_to_five_permut")
                                || order.getProperty().startsWith("three_to_six_permut")
                                ? "pf." : "c.";
                        return prefix + order.getProperty() + " " + order.getDirection().name();
                    })
                    .collect(Collectors.joining(", "));
        }


        Integer[] firstCycleArray = firstCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] secondCycleArray = secondCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] thirdCycleArray = thirdCycleCounts.keySet().toArray(new Integer[0]);

        rotorOrder = (rotorOrder == null || rotorOrder.length == 0) ? new Integer[0] : rotorOrder;
        rotorPosition = (rotorPosition == null || rotorPosition.length == 0) ? new Integer[0] : rotorPosition;

        StringBuilder frequencyChecks = new StringBuilder();

        firstCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                    AND cardinality(array_positions(p.one_to_four_permut, %d)) >= %d
            """, value, count)
                );
            }
        });

        secondCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                AND cardinality(array_positions(p.two_to_five_permut, %d)) >= %d
            """, value, count)
                );
            }
        });

        thirdCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                AND cardinality(array_positions(p.three_to_six_permut, %d)) >= %d
            """, value, count)
                );
            }
        });

        String frequencyClause = frequencyChecks.length() > 0 ? " " + frequencyChecks.toString() : "";


        String sql = String.format("""
                    WITH prefiltered AS (
                        SELECT *
                        FROM permutations p
                        WHERE (coalesce(cardinality(:firstCycleArray), 0) = 0 OR p.one_to_four_permut @> cast(:firstCycleArray AS integer[]))
                          AND (coalesce(cardinality(:secondCycleArray), 0) = 0 OR p.two_to_five_permut @> cast(:secondCycleArray AS integer[]))
                          AND (coalesce(cardinality(:thirdCycleArray), 0) = 0 OR p.three_to_six_permut @> cast(:thirdCycleArray AS integer[]))
                        %s
                    )
                    SELECT\s
                        pf.*,\s
                        c.*
                    FROM prefiltered pf
                    JOIN config c ON pf.permut_id = c.permut_id
                    WHERE (coalesce(cardinality(:rotorOrder), 0) = 0 OR c.rotor_order = cast(:rotorOrder AS integer[]))
                      AND (coalesce(cardinality(:rotorPosition), 0) = 0 OR c.rotor_position = cast(:rotorPosition AS integer[]))
                      %s
                    LIMIT :limit OFFSET :offset
               \s""", frequencyClause, orderByClause);
        System.out.println("SQL: \n" + sql);
        Session session = entityManager.unwrap(Session.class);

        NativeQuery<RotorCharacteristic> query = session.createNativeQuery(sql, RotorCharacteristic.class);
        query.setParameter("firstCycleArray", firstCycleArray);
        query.setParameter("secondCycleArray", secondCycleArray);
        query.setParameter("thirdCycleArray", thirdCycleArray);
        query.setParameter("rotorOrder", rotorOrder);
        query.setParameter("rotorPosition", rotorPosition);
        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());

        List<RotorCharacteristic> results = query.getResultList();

        return new PageImpl<>(results, pageable, totalCount);
    }


    @Override
    @Cacheable(value = "rotorCharacteristicCount", key = "#countCacheKey")
    public Long countRotorCharacteristics(
            Map<Integer, Integer> firstCycleCounts,
            Map<Integer, Integer> secondCycleCounts,
            Map<Integer, Integer> thirdCycleCounts,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            RotorCharacteristicCountCacheKey countCacheKey
    ) {

        Integer[] firstCycleArray = firstCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] secondCycleArray = secondCycleCounts.keySet().toArray(new Integer[0]);
        Integer[] thirdCycleArray = thirdCycleCounts.keySet().toArray(new Integer[0]);

        rotorOrder = (rotorOrder == null || rotorOrder.length == 0) ? new Integer[0] : rotorOrder;
        rotorPosition = (rotorPosition == null || rotorPosition.length == 0) ? new Integer[0] : rotorPosition;

        StringBuilder frequencyChecks = new StringBuilder();

        firstCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                    AND cardinality(array_positions(p.one_to_four_permut, %d)) >= %d
            """, value, count)
                );
            }
        });

        secondCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                AND cardinality(array_positions(p.two_to_five_permut, %d)) >= %d
            """, value, count)
                );
            }
        });

        thirdCycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                frequencyChecks.append(
                        String.format("""
                AND cardinality(array_positions(p.three_to_six_permut, %d)) >= %d
            """, value, count)
                );
            }
        });


        String frequencyClause = frequencyChecks.length() > 0 ? " " + frequencyChecks.toString() : "";

        // Count-Query (für Gesamtzahl)
        String countSql = String.format("""
                    WITH prefiltered AS (
                        SELECT *
                        FROM permutations p
                        WHERE (coalesce(cardinality(:firstCycleArray), 0) = 0 OR p.one_to_four_permut @> cast(:firstCycleArray AS integer[]))
                          AND (coalesce(cardinality(:secondCycleArray), 0) = 0 OR p.two_to_five_permut @> cast(:secondCycleArray AS integer[]))
                          AND (coalesce(cardinality(:thirdCycleArray), 0) = 0 OR p.three_to_six_permut @> cast(:thirdCycleArray AS integer[]))
                          %s
                    )
                    SELECT COUNT(*)
                    FROM prefiltered pf
                    JOIN config c ON pf.permut_id = c.permut_id
                    WHERE (coalesce(cardinality(:rotorOrder), 0) = 0 OR c.rotor_order = cast(:rotorOrder AS integer[]))
                      AND (coalesce(cardinality(:rotorPosition), 0) = 0 OR c.rotor_position = cast(:rotorPosition AS integer[]))
                """, frequencyClause);
        System.out.println("countSQL: \n" + countSql);
        Session session = entityManager.unwrap(Session.class);

        // Count-Query
        NativeQuery<Long> countQuery = session.createNativeQuery(countSql, Long.class);
        countQuery.setParameter("firstCycleArray", firstCycleArray);
        countQuery.setParameter("secondCycleArray", secondCycleArray);
        countQuery.setParameter("thirdCycleArray", thirdCycleArray);
        countQuery.setParameter("rotorOrder", rotorOrder);
        countQuery.setParameter("rotorPosition", rotorPosition);

        return countQuery.getSingleResult();
    }
}

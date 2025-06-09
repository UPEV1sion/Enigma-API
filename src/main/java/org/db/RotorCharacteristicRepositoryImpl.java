package org.db;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.jetbrains.annotations.NotNull;
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

    private static Map<Integer, Integer> countFrequencies(Integer[] values) {
        return Arrays.stream(values)
                .collect(Collectors.toMap(
                        v -> v,
                        v -> 1,
                        Integer::sum
                ));
    }

    private String buildDynamicWhereClause(Integer[] firstCycle, Integer[] secondCycle, Integer[] thirdCycle) {
        List<String> whereConditions = new ArrayList<>();

        if (firstCycle != null && firstCycle.length > 0) {
            whereConditions.add("p.one_to_four_permut @> cast(:firstCycle AS integer[])");
        }
        if (secondCycle != null && secondCycle.length > 0) {
            whereConditions.add("p.two_to_five_permut @> cast(:secondCycle AS integer[])");
        }
        if (thirdCycle != null && thirdCycle.length > 0) {
            whereConditions.add("p.three_to_six_permut @> cast(:thirdCycle AS integer[])");
        }

        if (whereConditions.isEmpty()) {
            return "";
        }

        return "WHERE " + String.join("\n  AND ", whereConditions);
    }

    private void appendFrequencyChecks(StringBuilder frequencyChecks,
                                       @NotNull Map<Integer, Integer> cycleCounts,
                                       String prefix) {
        int sumOfValues = cycleCounts.entrySet().stream()
                .mapToInt(e -> e.getKey() * e.getValue())
                .sum();

        boolean useEqualsForAll = (sumOfValues == 13);

        cycleCounts.forEach((value, count) -> {
            if (count >= 2) {
                if (!frequencyChecks.isEmpty()) {
                    frequencyChecks.append(" AND ");
                }
                String operator = (useEqualsForAll || value >= 7) ? "=" : ">=";

                frequencyChecks.append(String.format(
                        "%s_%d %s %d",
                        prefix, value, operator, count));
            }
        });
    }

    private @NotNull String buildRotorConfigClause(Integer[] rotorOrder, Integer[] rotorPosition) {
        List<String> conditions = new ArrayList<>();

        if (rotorOrder != null && rotorOrder.length > 0) {
            conditions.add("c.rotor_order = cast(:rotorOrder AS integer[])");
        }

        if (rotorPosition != null && rotorPosition.length > 0) {
            conditions.add("c.rotor_position = cast(:rotorPosition AS integer[])");
        }

        if (conditions.isEmpty()) {
            return ""; // Keine Bedingungen nötig
        }

        return "WHERE " + String.join(" AND ", conditions);
    }


    @Autowired
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "rotorCharacteristics", key = "#cacheKey")
    public Page<RotorCharacteristic> findRotorCharacteristic(
            Integer[] firstCycle,
            Integer[] secondCycle,
            Integer[] thirdCycle,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            @org.jetbrains.annotations.NotNull Pageable pageable,
            RotorCharacteristicCacheKey cacheKey,
            Long totalCount
    ) {
        Map<Integer, Integer> firstCycleCounts = countFrequencies(firstCycle);
        Map<Integer, Integer> secondCycleCounts = countFrequencies(secondCycle);
        Map<Integer, Integer> thirdCycleCounts = countFrequencies(thirdCycle);

        rotorOrder = (rotorOrder == null || rotorOrder.length == 0) ? new Integer[0] : rotorOrder;
        rotorPosition = (rotorPosition == null || rotorPosition.length == 0) ? new Integer[0] : rotorPosition;

        String roughClause = buildDynamicWhereClause(firstCycle, secondCycle, thirdCycle);

        StringBuilder frequencyChecks = new StringBuilder();
        appendFrequencyChecks(frequencyChecks, firstCycleCounts, "one");
        appendFrequencyChecks(frequencyChecks, secondCycleCounts, "two");
        appendFrequencyChecks(frequencyChecks, thirdCycleCounts, "three");

        String frequencyClause = "";
        if (!frequencyChecks.isEmpty()) {
            frequencyClause = "WHERE " + frequencyChecks;
        }


        String rotorConfigClause = buildRotorConfigClause(rotorOrder, rotorPosition);

        Sort sort = pageable.getSort();
        String orderByClause = "";

        if (!sort.isUnsorted()) {
            orderByClause = " ORDER BY " + sort.stream()
                    .map(order -> {
                        String property = order.getProperty();
                        String direction = order.getDirection().name();

                        return switch (property) {
                            case "rotor_order" -> "c.rotor_id " + direction;
                            case "rotor_position" -> "c.position_id " + direction;
                            case "one_to_four_permut", "two_to_five_permut", "three_to_six_permut" ->
                                    "pf." + property + " " + direction;
                            default -> "c." + property + " " + direction;
                        };
                    })
                    .collect(Collectors.joining(", "));
        }

        String sql = String.format("""
        WITH rough_filtered AS (
        SELECT *
        FROM permutation_counts p
        %s
        ),
        exact_filtered AS (
        SELECT *
        FROM rough_filtered
        %s
        )
        SELECT
        ef.*,
        c.permut_id AS c_permut_id,
        c.rotor_id,
        c.rotor_order,
        c.position_id,
        c.rotor_position
        FROM exact_filtered ef
        JOIN config c ON ef.permut_id = c.permut_id
        %s
        %s
        LIMIT :limit OFFSET :offset
        """, roughClause, frequencyClause, rotorConfigClause, orderByClause);


        System.out.println("SQL: \n" + sql);

        Session session = entityManager.unwrap(Session.class);
        NativeQuery<RotorCharacteristic> query = session.createNativeQuery(sql, RotorCharacteristic.class);

        if (firstCycle.length > 0) {
            query.setParameter("firstCycle", firstCycle);
        }
        if (secondCycle.length > 0) {
            query.setParameter("secondCycle", secondCycle);
        }
        if (thirdCycle.length > 0) {
            query.setParameter("thirdCycle", thirdCycle);
        }

        if (rotorOrder.length > 0) {
            query.setParameter("rotorOrder", rotorOrder);
        }
        if (rotorPosition.length > 0) {
            query.setParameter("rotorPosition", rotorPosition);
        }

        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());


        long startTime = System.nanoTime();

        List<RotorCharacteristic> results = query.getResultList();

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("Query executed in " + durationMs + " ms");


        return new PageImpl<>(results, pageable, totalCount);
    }


    @Override
    @Cacheable(value = "rotorCharacteristicCount", key = "#countCacheKey")
    public Long countRotorCharacteristics(
            Integer[] firstCycle,
            Integer[] secondCycle,
            Integer[] thirdCycle,
            Integer[] rotorOrder,
            Integer[] rotorPosition,
            RotorCharacteristicCountCacheKey countCacheKey
    ) {
        Map<Integer, Integer> firstCycleCounts = countFrequencies(firstCycle);
        Map<Integer, Integer> secondCycleCounts = countFrequencies(secondCycle);
        Map<Integer, Integer> thirdCycleCounts = countFrequencies(thirdCycle);

        rotorOrder = (rotorOrder == null || rotorOrder.length == 0) ? new Integer[0] : rotorOrder;
        rotorPosition = (rotorPosition == null || rotorPosition.length == 0) ? new Integer[0] : rotorPosition;

        String roughClause = buildDynamicWhereClause(firstCycle, secondCycle, thirdCycle);

        StringBuilder frequencyChecks = new StringBuilder();
        appendFrequencyChecks(frequencyChecks, firstCycleCounts, "one");
        appendFrequencyChecks(frequencyChecks, secondCycleCounts, "two");
        appendFrequencyChecks(frequencyChecks, thirdCycleCounts, "three");

        String frequencyClause = "";
        if (!frequencyChecks.isEmpty()) {
            frequencyClause = "WHERE " + frequencyChecks.toString();
        }
        String rotorConfigClause = buildRotorConfigClause(rotorOrder, rotorPosition);

        String countSql = String.format("""
        WITH rough_filtered AS (
        SELECT *
        FROM permutation_counts p
        %s
        ),
        exact_filtered AS (
        SELECT *
        FROM rough_filtered
        %s
        )
       SELECT COUNT(*)
       FROM exact_filtered ef
       JOIN config c ON ef.permut_id = c.permut_id
        %s
       \s""", roughClause, frequencyClause, rotorConfigClause);


        System.out.println("countSQL: \n" + countSql);

        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Long> countQuery = session.createNativeQuery(countSql, Long.class);

        if (firstCycle.length > 0) {
            countQuery.setParameter("firstCycle", firstCycle);
        }
        if (secondCycle.length > 0) {
            countQuery.setParameter("secondCycle", secondCycle);
        }
        if (thirdCycle.length > 0) {
            countQuery.setParameter("thirdCycle", thirdCycle);
        }
        if (rotorOrder.length > 0) {
            countQuery.setParameter("rotorOrder", rotorOrder);
        }
        if (rotorPosition.length > 0) {
            countQuery.setParameter("rotorPosition", rotorPosition);
        }

        long startTime = System.nanoTime();

        long result = countQuery.getSingleResult();

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("CountQuery executed in " + durationMs + " ms");

        return result;
    }
}

package org.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Embeddable
public class PrimaryKey implements Serializable {
    @JsonProperty("rotor_position")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "rotor_position")
    private int[] rotorPosition;

    @JsonProperty("rotor_order")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "rotor_order")
    private int[] rotorOrder;

    public int[] getRotorPosition() {
        return rotorPosition;
    }

    public int[] getRotorOrder() {
        return rotorOrder;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PrimaryKey that
                && Arrays.equals(rotorPosition, that.rotorPosition) && Arrays.equals(rotorOrder, that.rotorOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(rotorPosition), Arrays.hashCode(rotorOrder));
    }

    @Override
    public String toString() {
        return "PrimaryKey{" +
                "rotor_position=" + Arrays.toString(rotorPosition) +
                ", rotor_order=" + Arrays.toString(rotorOrder) +
                '}';
    }
}

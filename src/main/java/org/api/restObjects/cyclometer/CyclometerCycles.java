package org.api.restObjects.cyclometer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.api.restObjects.validators.catalogue.annotations.ValidCyclometerCycle;

import java.util.Arrays;

@Embeddable
public record CyclometerCycles (
        @JsonProperty("one_to_four_permut")
        @Column(name = "one_to_four_permut")
        @ValidCyclometerCycle
        int[] firstToThird,

        @JsonProperty("two_to_five_permut")
        @Column(name = "two_to_five_permut")
        @ValidCyclometerCycle
        int[] secondToFourth,

        @JsonProperty("three_to_six_permut")
        @Column(name = "three_to_six_permut")
        @ValidCyclometerCycle
        int[] thirdToSixth
){

    public CyclometerCycles() {
        this(new int[0], new int[0], new int[0]);
    }


    @Override
    public String toString() {
        return "CyclometerCycles{" +
                "firstToThird=" + Arrays.toString(firstToThird) +
                ", secondToFourth=" + Arrays.toString(secondToFourth) +
                ", thirdToSixth=" + Arrays.toString(thirdToSixth) +
                '}';
    }
}

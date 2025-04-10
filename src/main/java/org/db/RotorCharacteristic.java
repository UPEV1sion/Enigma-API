package org.db;

import jakarta.persistence.*;
import org.api.restObjects.cyclometer.CyclometerCycles;

@Entity
@Table(name = "cycles")
public class RotorCharacteristic {

    public RotorCharacteristic() {}

    @Embedded
    private CyclometerCycles cycles;

    @Id
    private PrimaryKey enigmaConfiguration;

    public CyclometerCycles getCycles() {return cycles; }

    public PrimaryKey getEnigmaConfiguration() {
        return enigmaConfiguration;
    }

    @Override
    public String toString() {
        return "RotorCharacteristic{" +
                "cycles=" + cycles +
                ", enigma_configuration=" + enigmaConfiguration +
                '}';
    }
}

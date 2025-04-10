package org.nativeCInterface;

import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.cyclometer.CyclometerRequest;

import java.util.Optional;

public interface CyclometerConnector {
    Optional<CyclometerCycles> getCyclesFromCyclometer(final CyclometerRequest config);
}

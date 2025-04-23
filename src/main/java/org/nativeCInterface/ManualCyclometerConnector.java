package org.nativeCInterface;

import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;

import java.util.Optional;

public interface ManualCyclometerConnector {
    Optional<CyclometerCycles> getManualCyclesFromCyclometer(final ManualCyclometerRequest config);
}

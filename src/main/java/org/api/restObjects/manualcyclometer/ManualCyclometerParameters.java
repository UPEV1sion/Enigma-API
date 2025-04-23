package org.api.restObjects.manualcyclometer;

import org.api.restObjects.validators.cyclometer.annotations.ParametersValidDailyKeyCount;
import org.api.restObjects.validators.manualcyclometer.annotations.ParametersValidManualKey;

public record ManualCyclometerParameters(
        @ParametersValidDailyKeyCount
        String daily_key_count,
        @ParametersValidManualKey
        String[] manual_keys) {}




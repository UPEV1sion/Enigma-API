package org.api.restObjects.cyclometer;

import org.api.restObjects.validators.cyclometer.annotations.ParametersValidDailyKeyCount;

public record CyclometerParameters(
        @ParametersValidDailyKeyCount
        String daily_key_count) {}
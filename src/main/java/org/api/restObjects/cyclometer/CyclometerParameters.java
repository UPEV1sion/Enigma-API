package org.api.restObjects.cyclometer;

import org.api.restObjects.validators.cyclometer.annotations.ParametersValidDailyKeyCount;

public record CyclometerParameters(
        @ParametersValidDailyKeyCount
        Integer daily_key_count) {}
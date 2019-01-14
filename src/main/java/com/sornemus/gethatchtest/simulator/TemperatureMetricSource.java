package com.sornemus.gethatchtest.simulator;

import java.util.Random;

public class TemperatureMetricSource implements MetricSource {

    public int getMetric() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public String getMetricMeasure() {
        return "Celsius";
    }
}

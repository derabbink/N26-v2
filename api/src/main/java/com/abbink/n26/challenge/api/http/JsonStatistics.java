package com.abbink.n26.challenge.api.http;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A container type specifically designed to pass back-and-forth
 * between API clients in JSON format.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"sum", "avg", "max", "min", "count"})
public class JsonStatistics {
    public static final int SCALE = 2;

    private double avg;
    private int count;
    private Double max;
    private Double min;
    private double sum;

    public JsonStatistics(
            @JsonProperty("avg") double avg,
            @JsonProperty("count") int count,
            @JsonProperty("max") Double max,
            @JsonProperty("min") Double min,
            @JsonProperty("sum") double sum
    ) {
        this.avg = avg;
        this.count = count;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public int getCount() {
        return count;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public double getSum() {
        return sum;
    }
}

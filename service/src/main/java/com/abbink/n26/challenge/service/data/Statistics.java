package com.abbink.n26.challenge.service.data;

public class Statistics {
    public static final int SCALE = 2;

    private double avg;
    private double count;
    private Double max;
    private Double min;
    private double sum;

    public Statistics(double avg, double count, Double max, Double min, double sum) {
        this.avg = avg;
        this.count = count;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getCount() {
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

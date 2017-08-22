package com.abbink.n26.challenge.service.data;

import java.math.BigDecimal;

public class Statistics {
    private BigDecimal avg;
    private int count;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal sum;

    public Statistics(BigDecimal avg, int count, BigDecimal max, BigDecimal min, BigDecimal sum) {
        this.avg = avg;
        this.count = count;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getSum() {
        return sum;
    }
}

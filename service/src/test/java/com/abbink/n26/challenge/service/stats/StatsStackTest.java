package com.abbink.n26.challenge.service.stats;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class StatsStackTest {
    @Test
    public void rollingAverageWorks() {
        double[] values = new double[] {11, 42, 10, 76, 39, 23, 44, 79, 18, 100};
        double[] averages = new double[] {11, 26.5, 21, 34.75, 35.6, 33.5, 35, 40.5, 38, 44.2};
        assertThat(values.length, equalTo(averages.length));

        StatsStack stack = new StatsStack();
        assertThat(stack, empty());
        assertThat(stack.getAvg(), comparesEqualTo(BigDecimal.ZERO));

        // push things onto stack
        for (int i = 0; i < values.length; i++) {
            stack.push(BigDecimal.valueOf(values[i]));
            assertThat(stack.getAvg(), comparesEqualTo(BigDecimal.valueOf(averages[i])));
        }

        assertThat(stack.size(), equalTo(values.length));

        // pop things from stack
        for (int i = values.length - 2; i >= 0; i--) {
            stack.pop();
            assertThat(stack.getAvg(), comparesEqualTo(BigDecimal.valueOf(averages[i])));
        }
        stack.pop();

        assertThat(stack, empty());
        assertThat(stack.getAvg(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void rollingMinWorks() {
        double[] values = new double[] {50, 60, 40, 30, 20, 100, 200, 20};
        double[] minimums = new double[] {50, 50, 40, 30, 20, 20, 20, 20};

        StatsStack stack = new StatsStack();
        assertThat(stack, empty());
        assertThat(stack.getMin(), nullValue());

        // push things onto stack
        for (int i = 0; i < values.length; i++) {
            stack.push(BigDecimal.valueOf(values[i]));
            assertThat(stack.getMin(), comparesEqualTo(BigDecimal.valueOf(minimums[i])));
        }

        assertThat(stack.size(), equalTo(values.length));

        // pop things from stack
        for (int i = values.length - 2; i >= 0; i--) {
            stack.pop();
            assertThat(stack.getMin(), comparesEqualTo(BigDecimal.valueOf(minimums[i])));
        }
        stack.pop();

        assertThat(stack, empty());
        assertThat(stack.getMin(), nullValue());
    }

    @Test
    public void rollingMaxWorks() {
        double[] values = new double[] {10, 20, 10, 30, 40, 1, 2, 40};
        double[] maximums = new double[] {10, 20, 20, 30, 40, 40, 40, 40};

        StatsStack stack = new StatsStack();
        assertThat(stack, empty());
        assertThat(stack.getMax(), nullValue());

        // push things onto stack
        for (int i = 0; i < values.length; i++) {
            stack.push(BigDecimal.valueOf(values[i]));
            assertThat(stack.getMax(), comparesEqualTo(BigDecimal.valueOf(maximums[i])));
        }

        assertThat(stack.size(), equalTo(values.length));

        // pop things from stack
        for (int i = values.length - 2; i >= 0; i--) {
            stack.pop();
            assertThat(stack.getMax(), comparesEqualTo(BigDecimal.valueOf(maximums[i])));
        }
        stack.pop();

        assertThat(stack, empty());
        assertThat(stack.getMax(), nullValue());
    }

    @Test
    public void rollingSumWorks() {
        double[] values = new double[] {10, 20, 30, 40, 50};
        double[] sums = new double[] {10, 30, 60, 100, 150};

        StatsStack stack = new StatsStack();
        assertThat(stack, empty());
        assertThat(stack.getSum(), comparesEqualTo(BigDecimal.ZERO));

        // push things onto stack
        for (int i = 0; i < values.length; i++) {
            stack.push(BigDecimal.valueOf(values[i]));
            assertThat(stack.getSum(), comparesEqualTo(BigDecimal.valueOf(sums[i])));
        }

        assertThat(stack.size(), equalTo(values.length));

        // pop things from stack
        for (int i = values.length - 2; i >= 0; i--) {
            stack.pop();
            assertThat(stack.getSum(), comparesEqualTo(BigDecimal.valueOf(sums[i])));
        }
        stack.pop();

        assertThat(stack, empty());
        assertThat(stack.getSum(), comparesEqualTo(BigDecimal.ZERO));
    }
}

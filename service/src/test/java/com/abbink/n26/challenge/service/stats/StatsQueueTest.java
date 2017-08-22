package com.abbink.n26.challenge.service.stats;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class StatsQueueTest {
    @Test
    public void queueMechanismWorks() {
        double[] values = new double[] {11, 42, 10, 76, 39, 23, 44, 79, 18, 100};
        StatsQueue queue = new StatsQueue();
        assertThat(queue, empty());

        int addIndex = 0;
        int removeIndex = 0;
        addIndex = addToQueue(queue, values, addIndex, 3);
        removeIndex = removeFromQueue(queue, values, removeIndex, 2);
        addIndex = addToQueue(queue, values, addIndex, 2);
        removeIndex = removeFromQueue(queue, values, removeIndex, 2);
        addIndex = addToQueue(queue, values, addIndex, 4);
        removeIndex = removeFromQueue(queue, values, removeIndex, 5);
        addIndex = addToQueue(queue, values, addIndex, 1);
        removeIndex = removeFromQueue(queue, values, removeIndex, 1);

        assertThat(addIndex, equalTo(values.length));
        assertThat(removeIndex, equalTo(values.length));
        assertThat(queue, empty());
    }

    /**
     * @param addIndex index from where to start adding values
     * @param n how many values to add
     * @return the updated addIndex
     */
    private int addToQueue(StatsQueue queue, double[] values, int addIndex, int n) {
        int maxIndex = Math.min(addIndex + n, values.length);
        for (int i = addIndex; i < maxIndex; i++) {
            queue.add(BigDecimal.valueOf(values[i]));
        }
        return maxIndex;
    }

    /**
     * Removes values from the queue and asserts that they match the expected values
     * @param removeIndex index from where to start comparing values
     * @param n how many values to remove
     * @return the updated removeIndex
     */
    private int removeFromQueue(StatsQueue queue, double[] values, int removeIndex, int n) {
        int maxIndex = Math.min(removeIndex + n, values.length);
        for (int i = removeIndex; i < maxIndex; i++) {
            assertThat(queue.remove(), comparesEqualTo(BigDecimal.valueOf(values[i])));
        }
        return maxIndex;
    }

    @Test
    public void runningAverageWorks() {
        double[] values = new double[] {10, 30, 50};
        double[] buildupAverages = new double[] {10, 20, 30};
        double[] breakdownAverages = new double[] {30, 40, 50};
        assertThat(values.length, equalTo(buildupAverages.length));
        assertThat(values.length, equalTo(breakdownAverages.length));

        StatsQueue queue = new StatsQueue();
        assertThat(queue, empty());
        assertThat(queue.getAvg(), comparesEqualTo(BigDecimal.ZERO));

        // add things to queue
        for (int i = 0; i < values.length; i++) {
            queue.add(BigDecimal.valueOf(values[i]));
            assertThat(queue.getAvg(), comparesEqualTo(BigDecimal.valueOf(buildupAverages[i])));
        }

        assertThat(queue.size(), equalTo(values.length));

        // remove things from queue
        for (int i = 0; i < breakdownAverages.length ; i++) {
            assertThat(queue.getAvg(), comparesEqualTo(BigDecimal.valueOf(breakdownAverages[i])));
            queue.remove();
        }

        assertThat(queue, empty());
        assertThat(queue.getAvg(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void runningSumWorks() {
        double[] values = new double[] {10, 20, 30};
        double[] buildupSums = new double[] {10, 30, 60};
        double[] breakdownSums = new double[] {60, 50, 30};
        assertThat(values.length, equalTo(buildupSums.length));
        assertThat(values.length, equalTo(breakdownSums.length));

        StatsQueue queue = new StatsQueue();
        assertThat(queue, empty());
        assertThat(queue.getSum(), comparesEqualTo(BigDecimal.ZERO));

        // add things to queue
        for (int i = 0; i < values.length; i++) {
            queue.add(BigDecimal.valueOf(values[i]));
            assertThat(queue.getSum(), comparesEqualTo(BigDecimal.valueOf(buildupSums[i])));
        }

        assertThat(queue.size(), equalTo(values.length));

        // remove things from queue
        for (int i = 0; i < breakdownSums.length ; i++) {
            assertThat(queue.getSum(), comparesEqualTo(BigDecimal.valueOf(breakdownSums[i])));
            queue.remove();
        }

        assertThat(queue, empty());
        assertThat(queue.getSum(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void runningMinWorks() {
        double[] values = new double[] {50, 40, 70, 60, 30, 40, 50};
        double[] buildupMins = new double[] {50, 40, 40, 40, 30, 30, 30};
        double[] breakdownMins = new double[] {30, 30, 30, 30, 30, 40, 50};
        assertThat(values.length, equalTo(buildupMins.length));
        assertThat(values.length, equalTo(breakdownMins.length));

        StatsQueue queue = new StatsQueue();
        assertThat(queue, empty());
        assertThat(queue.getMin(), nullValue());

        // add things to queue
        for (int i = 0; i < values.length; i++) {
            queue.add(BigDecimal.valueOf(values[i]));
            assertThat(queue.getMin(), comparesEqualTo(BigDecimal.valueOf(buildupMins[i])));
        }

        assertThat(queue.size(), equalTo(values.length));

        // remove things from queue
        for (int i = 0; i < breakdownMins.length ; i++) {
            assertThat(queue.getMin(), comparesEqualTo(BigDecimal.valueOf(breakdownMins[i])));
            queue.remove();
        }

        assertThat(queue, empty());
        assertThat(queue.getMin(), nullValue());
    }

    @Test
    public void runningMaxWorks() {
        double[] values = new double[] {50, 40, 30, 60, 70, 50, 40};
        double[] buildupMaxs = new double[] {50, 50, 50, 60, 70, 70, 70};
        double[] breakdownMaxs = new double[] {70, 70, 70, 70, 70, 50, 40};
        assertThat(values.length, equalTo(buildupMaxs.length));
        assertThat(values.length, equalTo(breakdownMaxs.length));

        StatsQueue queue = new StatsQueue();
        assertThat(queue, empty());
        assertThat(queue.getMax(), nullValue());

        // add things to queue
        for (int i = 0; i < values.length; i++) {
            queue.add(BigDecimal.valueOf(values[i]));
            assertThat(queue.getMax(), comparesEqualTo(BigDecimal.valueOf(buildupMaxs[i])));
        }

        assertThat(queue.size(), equalTo(values.length));

        // remove things from queue
        for (int i = 0; i < breakdownMaxs.length ; i++) {
            assertThat(queue.getMax(), comparesEqualTo(BigDecimal.valueOf(breakdownMaxs[i])));
            queue.remove();
        }

        assertThat(queue, empty());
        assertThat(queue.getMax(), nullValue());
    }
}

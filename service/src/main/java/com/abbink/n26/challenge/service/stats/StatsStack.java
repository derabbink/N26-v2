package com.abbink.n26.challenge.service.stats;

import com.abbink.n26.challenge.service.data.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * A stack of {@link BigDecimal}s that maintains {@link Stats}, such as a min, max, avg, sum of all values kept in it.
 * {@link #push(BigDecimal)} and {@link #pop()} take O(1) time.
 */
public class StatsStack extends Stack<Transaction> implements Stats {
    public static final int SCALE = 7;

    private Stack<BigDecimal> minStack;
    private Stack<BigDecimal> maxStack;
    private BigDecimal sum;
    private BigDecimal avg;

    public StatsStack() {
        minStack = new Stack<>();
        maxStack = new Stack<>();
        sum = BigDecimal.ZERO;
        avg = BigDecimal.ZERO;
    }

    @Override
    public Transaction push(Transaction value) {
        BigDecimal amount = value.getAmount();
        if (isEmpty() || amount.compareTo(getMin()) < 0) {
            minStack.push(amount);
        } else {
            minStack.push(getMin());
        }

        if (isEmpty() || amount.compareTo(getMax()) > 0) {
            maxStack.push(amount);
        } else {
            maxStack.push(getMax());
        }

        sum = sum.add(amount);

        BigDecimal size = BigDecimal.valueOf(size());
        BigDecimal sizePlusOne = size.add(BigDecimal.ONE);
        BigDecimal addition = amount.divide(sizePlusOne, 2*SCALE, RoundingMode.HALF_UP);
        avg = avg.multiply(size.divide(sizePlusOne, 2*SCALE, RoundingMode.HALF_UP)).add(addition)
                .setScale(SCALE, BigDecimal.ROUND_HALF_UP);

        return super.push(value);
    }

    @Override
    public Transaction pop() {
        Transaction value = super.pop();
        BigDecimal amount = value.getAmount();

        minStack.pop();
        maxStack.pop();

        sum = sum.subtract(amount);

        if (size() == 0) {
            avg = BigDecimal.ZERO;
        } else {
            BigDecimal size = BigDecimal.valueOf(size());
            BigDecimal sizePlusOne = size.add(BigDecimal.ONE);
            BigDecimal subtraction = amount.divide(sizePlusOne, 2*SCALE,RoundingMode.HALF_UP);
            avg = avg.subtract(subtraction).multiply(sizePlusOne.divide(size, 2*SCALE, RoundingMode.HALF_UP))
                    .setScale(SCALE, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

    @Override
    public boolean remove(Object o) {
        // Not needed, so I won't implement it
        return false;
    }

    @Override
    public BigDecimal getMin() {
        try {
            return minStack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    @Override
    public BigDecimal getMax() {
        try {
            return maxStack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    @Override
    public BigDecimal getSum() {
        return sum;
    }

    @Override
    public BigDecimal getAvg() {
        return avg;
    }

    @Override
    public int getSize() {
        return getSize();
    }
}

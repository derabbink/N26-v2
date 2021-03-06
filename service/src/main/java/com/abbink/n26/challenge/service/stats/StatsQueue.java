package com.abbink.n26.challenge.service.stats;

import com.abbink.n26.challenge.service.data.Transaction;
import com.google.common.collect.Iterators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A queue based on two {@link StatsStack}s.
 * {@link #add(Transaction)} takes O(1) time, and adds to the {@link #inStack}.
 * {@link #remove()}/{@link #poll()} takes O(n) time, if the {@link #outStack} is empty. If so, it moves the
 * {@link #inStack} into the {@link #outStack} first (which takes that long). If the {@link #outStack} is not empty,
 * {@link #remove()}/{@link #poll()} takes O(1) time as well. The same goes for {@link #peek()}/{@link #element()}.
 */
public class StatsQueue implements Queue<Transaction>, Stats {
    private StatsStack inStack;
    private StatsStack outStack;

    public StatsQueue() {
        inStack = new StatsStack();
        outStack = new StatsStack();
    }

    @Override
    public boolean add(Transaction value) {
        inStack.push(value);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Transaction> collection) {
        for (Transaction value : collection) {
            inStack.push(value);
        }
        return true;
    }

    @Override
    public boolean offer(Transaction value) {
        add(value);
        return true;
    }

    @Override
    public Transaction remove() {
        Transaction result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public Transaction poll() {
        moveInStackToOutStackIfNeeded();
        if (outStack.isEmpty()) {
            return null;
        }
        return outStack.pop();
    }

    @Override
    public Transaction element() {
        Transaction result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public Transaction peek() {
        moveInStackToOutStackIfNeeded();
        if (outStack.isEmpty()) {
            return null;
        }
        return outStack.peek();
    }

    private void moveInStackToOutStackIfNeeded() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    @Override
    public int size() {
        return inStack.size() + outStack.size();
    }

    @Override
    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    // ####################
    // # more Queue stuff #
    // ####################

    @Override
    public boolean contains(Object o) {
        return inStack.contains(o) || outStack.contains(o);
    }

    /**
     * WARNING: This returns a destructive iterator: Iterating through all values empties out the queue.
     */
    @Override
    public Iterator<Transaction> iterator() {
        return new Iterator<Transaction>() {
            @Override
            public boolean hasNext() {
                return !StatsQueue.this.isEmpty();
            }

            @Override
            public Transaction next() {
                return StatsQueue.this.remove();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Iterators.toArray(iterator(), Transaction.class);
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        // We won't be needing this
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return outStack.remove(o) || inStack.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!inStack.contains(o) && !outStack.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!remove(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!contains(o) && !remove(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            remove();
        }
    }

    // #########
    // # Stats #
    // #########

    @Override
    public BigDecimal getMin() {
        if (inStack.isEmpty()) {
            return outStack.getMin();
        }
        if (outStack.isEmpty()) {
            return inStack.getMin();
        }
        return inStack.getMin().min(outStack.getMin());
    }

    @Override
    public BigDecimal getMax() {
        if (inStack.isEmpty()) {
            return outStack.getMax();
        }
        if (outStack.isEmpty()) {
            return inStack.getMax();
        }
        return inStack.getMax().min(outStack.getMax());
    }

    @Override
    public BigDecimal getSum() {
        return inStack.getSum().add(outStack.getSum());
    }

    @Override
    public BigDecimal getAvg() {
        if (inStack.isEmpty()) {
            return outStack.getAvg();
        }
        if (outStack.isEmpty()) {
            return inStack.getAvg();
        }
        return inStack.getAvg().multiply(BigDecimal.valueOf(inStack.size()))
                .add(outStack.getAvg().multiply(BigDecimal.valueOf(outStack.size())))
                .divide(BigDecimal.valueOf(inStack.size() + outStack.size()), 2*StatsStack.SCALE, RoundingMode.HALF_UP)
                .setScale(StatsStack.SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public int getSize() {
        return size();
    }
}

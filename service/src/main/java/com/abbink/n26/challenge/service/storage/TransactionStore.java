package com.abbink.n26.challenge.service.storage;

import com.abbink.n26.challenge.service.data.Transaction;

public interface TransactionStore {
    boolean contains(long id);
    void add(long id, Transaction t);
    void update(long id, Transaction t);
    Transaction get(long id);
    double getSubtreeSum(long rootId);
}

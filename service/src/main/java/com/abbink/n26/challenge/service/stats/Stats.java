package com.abbink.n26.challenge.service.stats;

import java.math.BigDecimal;

public interface Stats {
    BigDecimal getMin();
    BigDecimal getMax();
    BigDecimal getSum();
    BigDecimal getAvg();
    int getSize();
}

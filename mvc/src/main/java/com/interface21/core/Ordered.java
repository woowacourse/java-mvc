package com.interface21.core;

public interface Ordered {

    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /** Lower values have higher priority. */
    int getOrder();
}

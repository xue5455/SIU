package com.xue.eventbus;

/**
 * Created by zyl06 on 9/14/15.
 */
public enum HTPriority {
    EX_LOW (1),
    LOW (1 << 2),
    BIT_LOW (1 << 3),
    NORMAL (1 << 4),
    BIT_HIGH (1 << 5),
    HIGH (1 << 6),
    EX_HIGH (1 << 7);

    private int value;
    private HTPriority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}

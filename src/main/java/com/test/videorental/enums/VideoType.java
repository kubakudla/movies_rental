package com.test.videorental.enums;

import java.math.BigDecimal;

public enum VideoType {
    NEW_RELEASE, REGULAR, OLD;

    // should be easily configurable
    private int bonusPoint;

    private BigDecimal price;
}


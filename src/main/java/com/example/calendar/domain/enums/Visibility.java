package com.example.calendar.domain.enums;

public enum Visibility {
    PUBLIC(0), PRIVATE(1);

    private int code;

    Visibility(int code) {
        this.code = code;
    }
}

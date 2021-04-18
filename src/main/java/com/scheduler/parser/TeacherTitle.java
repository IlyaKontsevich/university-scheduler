package com.scheduler.parser;

import lombok.Getter;

public enum TeacherTitle {
    DOC("доц"),
    ST_PR("ст.пр."),
    PROFESSOR("проф");

    @Getter
    private final String russianValue;

    TeacherTitle(String russianValue) {
        this.russianValue = russianValue;
    }
}

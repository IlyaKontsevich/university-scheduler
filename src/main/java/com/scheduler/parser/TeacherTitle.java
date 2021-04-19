package com.scheduler.parser;

import lombok.Getter;

public enum TeacherTitle
{
    DOC( "доц.", 4 ),
    ST_PR( "ст.пр.", 6 ),
    PROFESSOR( "проф.", 5 ),
    ASSISTANT( "асс.", 4 );

    @Getter
    private final String russianValue;
    @Getter
    private final Integer size;

    TeacherTitle( String russianValue, Integer size )
    {
        this.russianValue = russianValue;
        this.size = size;
    }
}

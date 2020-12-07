package com.scheduler.timetable;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WeekDay
{
    MONDAY( "понедельник" ),
    TUESDAY( "вторник" ),
    WEDNESDAY( "среда" ),
    THURSDAY( "четверг" ),
    FRIDAY( "пятница" ),
    SUNDAY( "суббота" );

    private String russianName;

    WeekDay( String russianName )
    {
        this.russianName = russianName;
    }

    public static WeekDay getByRussianName( String russianName )
    {
        return Arrays.stream( WeekDay.values() )
                     .filter( weekDay -> weekDay.getRussianName().equals( russianName ) )
                     .findFirst()
                     .orElseThrow( () -> new RuntimeException( "Unable to find WeekDay with russianName = " + russianName ) );
    }

}

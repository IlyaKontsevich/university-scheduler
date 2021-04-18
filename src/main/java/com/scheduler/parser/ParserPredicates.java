package com.scheduler.parser;

import com.scheduler.timetable.WeekDay;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.scheduler.parser.ParserStringUtils.*;
import static org.apache.commons.lang3.StringUtils.containsAny;

public enum ParserPredicates
{
    IS_GROUP_NAME_ROW( row -> {
        List<String> groupNames = splitNotEmpty( row, "\\|" );
        String groupName = groupNames.get( 0 );
        return groupNames.size() > 1 && groupName.length() < 10 && isMatchesValidRegex( groupName, GROUP_NAME_REGEXES );
    } ),
    IS_ROW_START_WITH_WEEK_DAY( row -> StringUtils.startsWithAny( row,
        Arrays.stream( WeekDay.values() ).map( WeekDay::getRussianName ).toArray( String[]::new ) ) ),

    IS_ROW_START_WITH_LESSON_TIME( row -> row.matches( LESSON_TIME_REGEX ) ),

    IS_CELL_START_WITH_ADDITIONAL_TIME( cell -> cell.matches( ADDITIONAl_LESSON_TIME_REGEX ) ),
    IS_CELL_START_WITH_INITIALS( cell -> cell.matches( INITIALS_REGEX ) ),
    IS_CELL_CONTAINS_TEACHER_TITLE( cell -> containsAny( cell, Arrays.stream( TeacherTitle.values() )
                                                                     .map( TeacherTitle::getRussianValue )
                                                                     .toArray( CharSequence[]::new ) ) ),
    IS_CELL_CONTAINS_SURNAME(cell -> cell.matches( SURNAME_REGEX ));

    private final Predicate<String> predicate;

    public boolean test( String row )
    {
        return predicate.test( row );
    }

    ParserPredicates( Predicate<String> predicate )
    {
        this.predicate = predicate;
    }

}

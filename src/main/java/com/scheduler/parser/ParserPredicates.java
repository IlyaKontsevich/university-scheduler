package com.scheduler.parser;

import com.scheduler.timetable.WeekDay;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.scheduler.parser.ParserStringUtils.*;

public enum ParserPredicates
{
    IS_GROUP_NAME_ROW( row -> {
        List<String> groupNames = splitNotEmpty( row, "\\|" );
        String groupName = groupNames.get( 0 );
        return groupNames.size() > 1 && groupName.length() < 10 && isMatchesValidRegex( groupName, ParserStringUtils.REGEX_FOR_GROUP_NAMES );
    } ),
    IS_ROW_START_WITH_WEEK_DAY( row -> StringUtils.startsWithAny( row,
        Arrays.stream( WeekDay.values() ).map( WeekDay::getRussianName ).toArray( String[]::new ) ) ),

    IS_ROW_START_WITH_LESSON_TIME( row -> row.matches( REGEX_FOR_LESSON_TIME ) );

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

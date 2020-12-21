package com.scheduler.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserStringUtils
{
    public static final List<String> GROUP_NAME_REGEXES = List.of( "^[А-Я]-.*", "^[А-Я]{2}-.*", "^[А-Я]{3}-.*" );
    public static final String LESSON_TIME_REGEX = "^[|][0-9]{2}[.][0-9]{2}-[0-9]{2}[.][0-9]{2}.*";
    public static final String BLANK_ROW_REGEX = "^[|]*$";
    public static final String ADDITIONAl_LESSON_TIME_REGEX = "^[0-9]{2}[:][0-9]{2}-[0-9]{2}[:][0-9]{2}.*";
    public static final Map<Integer, String> SIZE_OF_AND_CLASSROOM_NAME_REGEXES = Map.of(
        6, "^[0-9][/][0-9]{3}[а-я].*",
        5, "^[0-9][/][0-9]{3}.*",
        4, "^[0-9]{3}[а-я].*",
        3, "^[0-9]{3}.*" );
    public static final String INITIALS_REGEX = "^[А-Я][.][А-Я][.].*";

    private ParserStringUtils()
    {
    }

    public static boolean isMatchesValidRegex( String arg, List<String> validRegexes )
    {
        return validRegexes.stream().anyMatch( arg::matches );
    }

    public static List<String> splitNotEmpty( String arg, String regex )
    {
        return Arrays.stream( arg.split( regex ) ).filter( s -> !s.isBlank() ).collect( Collectors.toList() );
    }
}

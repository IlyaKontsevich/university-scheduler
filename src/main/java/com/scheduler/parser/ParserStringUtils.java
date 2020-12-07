package com.scheduler.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParserStringUtils
{
    public static final List<String> REGEX_FOR_GROUP_NAMES = List.of( "^[А-Я]-.*", "^[А-Я]{2}-.*", "^[А-Я]{3}-.*" );
    public static final String REGEX_FOR_LESSON_TIME = "^[|][0-9]{2}[.][0-9]{2}-[0-9]{2}[.][0-9]{2}.*";
    public static final String REGEX_FOR_BLANK_ROW = "^[|]*$";

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

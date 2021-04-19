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
    public static final Integer ADDITIONAL_LESSON_TIME_SIZE = 11;
    public static final Map<Integer, String> SIZE_OF_AND_CLASSROOM_NAME_REGEXES_WITH_LETTERS = Map.of(
        6, "^[0-9][/][0-9]{3}[а-я].*",
        4, "^[0-9]{3}[а-я].*" );
    public static final Map<Integer, String> SIZE_OF_AND_CLASSROOM_NAME_REGEXES = Map.of(
        5, "^[0-9][/][0-9]{3}.*",
        3, "^[0-9]{3}.*" );
    public static final String START_WITH_INITIALS_REGEX = "^[А-Яа-я][.][А-Яа-я].*";
    public static final String START_WITH_INITIALS_WITH_ONE_LETTER_REGEX = "^[А-Я][.][ ].*|^[А-Я][.]*$|^[а-я][.]*$";
    public static final String CONTAINS_INITIALS_REGEX = ".*[А-Я][.][А-Я][.].*";
    public static final String SURNAME_REGEX = "^[А-Я]+[а-я]{2}.*";
    public static final String STRING_WITH_NUMBERS_REGEX = ".*[0-9].*";
    public static final String REGEX_TWO_FIRST_WORD_WITH_CAPITAL_LETTERS = "^[А-Я]+[а-я]*[ ]+[А-Я].*";
    public static final String REGEX_FIRST_WORD_IS_ABBREVIATION = "^[А-Я]\\p{L}*[А-Я]\\p{L}.*";

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

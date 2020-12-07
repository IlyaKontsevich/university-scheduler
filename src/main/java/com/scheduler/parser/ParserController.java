package com.scheduler.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping( "/parser" )
@RequiredArgsConstructor
public class ParserController
{
    private final ParserService parserService;

    @GetMapping( "/parse" )
    public String testS3Connection() throws IOException, InvalidFormatException
    {
        parserService.parse();
        return "Work";
    }

}

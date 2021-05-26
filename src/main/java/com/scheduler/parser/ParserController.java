package com.scheduler.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping( "/parser" )
@RequiredArgsConstructor
public class ParserController
{
    private final XlsParserService xlsParserService;
    private final DocParsesService docParsesService;

    @PreAuthorize( "hasAuthority('admin_write')")
    @PostMapping( "/parse/xls" )
    public String loadScheduler( @RequestBody MultipartFile file ) throws IOException, InvalidFormatException, URISyntaxException
    {
        return xlsParserService.parse( file.getInputStream() );
    }

    @PreAuthorize( "hasAuthority('admin_write')")
    @PostMapping( "/parse/doc" )
    public String loadStudents( @RequestBody MultipartFile file ) throws IOException, InvalidFormatException, URISyntaxException
    {
        return docParsesService.parse( file.getInputStream() );
    }
}

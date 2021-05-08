package com.scheduler.parser;

import com.scheduler.parser.temporary.StudentTmp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.scheduler.parser.ParserStringUtils.REGEX_START_WITH_NUMBERS;

@Service
@RequiredArgsConstructor
public class DocParsesService
{
    private final DocStoreService storeService;

    public String parse( InputStream inputStream ) throws IOException
    {
        Map<String, List<StudentTmp>> groupNameStudentsMap;
        HWPFDocument document = new HWPFDocument( inputStream );
        WordExtractor we = new WordExtractor( document );
        String[] paragraphs = we.getParagraphText();
        System.out.println( "Total no of paragraph " + paragraphs.length );
        String currentGroupName = "";
        List<StudentTmp> students = new ArrayList<>();
        for( String para : paragraphs )
        {
            if( StringUtils.containsIgnoreCase( para, "Группа" ) )
            {
                currentGroupName = para.substring( 6 ).replace( ":", "" ).trim();

            }
            para = para.replace( "\n", "" ).replace( "\r", "" );
            if( para.matches( REGEX_START_WITH_NUMBERS ) )
            {
                addStudent( currentGroupName, para, students );
            }
            System.out.println( para );
        }
        inputStream.close();
        groupNameStudentsMap = students.stream().collect( Collectors.groupingBy( StudentTmp::getGroupName, Collectors.toList() ) );
        groupNameStudentsMap.forEach( ( group, studentsTmp ) -> {
            for( int i = 0; i < studentsTmp.size(); i++ )
            {
                StudentTmp studentTmp = studentsTmp.get( i );
                boolean isFirstSubGroup = ( studentsTmp.size() - ( studentsTmp.size() - i ) ) < studentsTmp.size() / 2;
                if( isFirstSubGroup )
                {
                    studentTmp.setSubGroupNumber( 0 );
                }
                else
                {
                    studentTmp.setSubGroupNumber( 1 );
                }
            }
        } );
        storeService.store( students );
        return "students stored successfully";
    }

    private void addStudent( String currentGroupName, String data, List<StudentTmp> students )
    {
        data = data.substring( data.indexOf( "." ) + 1 ); //cat 3.
        String[] strings = data.split( " " );
        String name = strings[0] + " " + strings[1] + " " + strings[2].replace( ",", "" );
        String[] loginStrings = data.substring( data.indexOf( "логин" ) + 5 )
                                    .replace( ":", "" )
                                    .replace( "-", "" )
                                    .trim().split( " " );
        String login = loginStrings[0];
        String[] passwordStrings = data.substring( data.indexOf( "пароль" ) + 6 )
                                       .replace( ":", "" )
                                       .replace( "-", "" )
                                       .trim().split( " " );
        String password = passwordStrings[0];
        StudentTmp studentTmp = new StudentTmp();
        studentTmp.setGroupName( currentGroupName );
        studentTmp.setName( name );
        studentTmp.setEmail( login );
        studentTmp.setPassword( password );
        students.add( studentTmp );
    }
}

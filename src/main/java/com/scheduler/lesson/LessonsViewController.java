package com.scheduler.lesson;

import com.scheduler.lesson.service.LessonsViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping( "/timetable" )
public class LessonsViewController
{
    private final LessonsViewService lessonsViewService;

    @GetMapping( "/sub-group" )
    public ResponseEntity<?> getSubGroupLessonView(
        @RequestParam( name = "name") final String name,
        @RequestParam( name = "number") final Long number )
    {
        return ResponseEntity.ok( lessonsViewService.getSubGroupLessonsViewBySubGroupId( name, number ) );
    }

    @GetMapping( "/teacher" )
    public ResponseEntity<?> getSubGroupLessonView(
        @RequestParam( name = "name") final String name )
    {
        return ResponseEntity.ok( lessonsViewService.getTeacherLessonsByTeacherName( name ) );
    }
}

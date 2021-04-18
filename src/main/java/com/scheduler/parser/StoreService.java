package com.scheduler.parser;

import com.scheduler.classroom.Classroom;
import com.scheduler.classroom.ClassroomRepository;
import com.scheduler.group.Group;
import com.scheduler.group.GroupRepository;
import com.scheduler.group.subgroup.SubGroup;
import com.scheduler.group.subgroup.SubGroupRepository;
import com.scheduler.hull.Hull;
import com.scheduler.hull.HullRepository;
import com.scheduler.lesson.*;
import com.scheduler.parser.temporary.DataPerGroup;
import com.scheduler.teacher.Teacher;
import com.scheduler.teacher.TeacherRepository;
import com.scheduler.timetable.*;
import com.scheduler.university.University;
import com.scheduler.university.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService
{
    private final GroupRepository groupRepository;
    private final SubGroupRepository subGroupRepository;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final TimeTableRepository timeTableRepository;
    private final TimeTableLessonRepository timeTableLessonRepository;
    private final HullRepository hullRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherLessonRepository teacherLessonRepository;
    private final SubGroupLessonRepository subGroupLessonRepository;
    private final UniversityRepository universityRepository;
    private University university;

    public void storeData( Collection<DataPerGroup> data )
    {
        university = createAndStoreUniversity();
        data.forEach( dataPerGroup -> {
            dataPerGroup.getDataPerWeekDays().forEach( dataPerWeekDay -> {
                dataPerWeekDay.getDataPerLessonList().forEach( dataPerLessonTime -> {
                    dataPerLessonTime.getLessons().forEach( lessonVO -> {
                        Classroom classroom = createAndSaveClassroom( lessonVO );
                        TimeTable timeTable = createAndSaveTimeTable( lessonVO, dataPerWeekDay.getWeekDay() );
                        Lesson lesson = new Lesson();
                        lesson.setUniversityId( university.getId() );
                        lesson.setName( Optional.ofNullable( lessonVO.getName() ).orElse( "UNKNOWN" ) );
                        lesson.setClassroom( classroom );
                        lesson.setTimeTable( timeTable );
                        lesson = lessonRepository.save( lesson );
                        Teacher teacher = createAndSaveTeacher( lessonVO, lesson );
                        createAndSaveGroup( dataPerGroup.getGroupName(), lesson, teacher, lessonVO );
                    } );
                } );
            } );
        } );
    }

    private University createAndStoreUniversity()
    {
        University university = universityRepository.findByName( "БрГТУ" );//todo change
        if( university == null )
        {
            university = new University();
            university.setName( "БрГТУ" );
            university.setAddress( "ул.Московская 267" );
            university = universityRepository.save( university );
        }
        return university;
    }

    private Teacher createAndSaveTeacher( LessonVO lessonVO, Lesson lesson )
    {
        Teacher teacher = teacherRepository.findByName( lessonVO.getTeacher() );
        if( teacher == null )
        {
            teacher = new Teacher();

            teacher.setUniversityId( university.getId() );
            teacher.setName( Optional.ofNullable( lessonVO.getTeacher() ).orElse( "UNKNOWN" ) );
            teacher = teacherRepository.save( teacher );
        }
        TeacherLesson teacherLesson = new TeacherLesson();
        teacherLesson.setTeacher( teacher );
        teacherLesson.setUniversityId( university.getId() );
        teacherLesson.setLesson( lesson );
        teacherLesson = teacherLessonRepository.save( teacherLesson );
        teacher.getLessons().add( teacherLesson );
        return teacherRepository.save( teacher );
    }

    private TimeTable createAndSaveTimeTable( LessonVO lessonVO, WeekDay weekDay )
    {
        TimeTable timeTable = timeTableRepository.findByName( lessonVO.getName() );
        if( timeTable == null )
        {
            timeTable = new TimeTable();
            timeTable.setDates( lessonVO.getDates() );
            timeTable.setWeek( lessonVO.getWeekType() );
            timeTable.setWeekDay( weekDay );
            timeTable.setUniversityId( university.getId() );
            timeTable.setName( Optional.ofNullable( lessonVO.getName() ).orElse( UUID.randomUUID().toString() ) );//todo temporary
        }

        String lessonTime = lessonVO.getLessonTime();
        String startTime = lessonVO.getLessonTime().substring( 0, lessonTime.indexOf( "-" ) );
        String endTime = lessonVO.getLessonTime().substring( lessonTime.indexOf( "-" ) );
        TimeTableLesson timeTableLesson = timeTableLessonRepository.findByStartTime( startTime );
        if( timeTableLesson == null )
        {
            timeTableLesson = new TimeTableLesson();
            timeTableLesson.setStartTime( startTime );
            timeTableLesson.setEndTime( endTime );
            timeTableLesson.setUniversityId( university.getId() );
            timeTableLesson = timeTableLessonRepository.save( timeTableLesson );
        }
        timeTable.setTimeTableLesson( timeTableLesson );
        return timeTableRepository.save( timeTable );
    }

    private Classroom createAndSaveClassroom( LessonVO lessonVO )
    {
        String classroomName = Optional.ofNullable( lessonVO.getClassroomName() ).orElse( "UNKNOWN" );
        long hullNumber = 1L;
        if( StringUtils.contains( classroomName, "/" ) )
        {
            hullNumber = Long.parseLong( classroomName.substring( 0, classroomName.indexOf( "/" ) ) );
            classroomName = classroomName.substring( classroomName.indexOf( "/" ) );
        }
        Hull hull = hullRepository.findByNumber( hullNumber );
        if( hull == null )
        {
            hull = new Hull();
            hull.setUniversityId( university.getId() );
            hull.setAddress( university.getAddress() );
            hull.setNumber( hullNumber );
            hull = hullRepository.save( hull );
        }
        Classroom classroom = classroomRepository.findByName( classroomName );
        if( classroom == null )
        {
            classroom = new Classroom();
            classroom.setHull( hull );
            classroom.setUniversityId( university.getId() );
            classroom.setCapacity( 100L );//todo
            classroom.setName( classroomName );
            return classroomRepository.save( classroom );
        }
        return classroom;
    }

    private Group createAndSaveGroup( String groupName, Lesson lesson, Teacher teacher, LessonVO lessonVO )
    {
        Group group = groupRepository.findByName( groupName );
        if( group == null )
        {
            group = new Group();
            group.setName( groupName );
            group.setUniversityId( university.getId() );
            group = groupRepository.save( group );
        }

        SubGroup subGroup1 = subGroupRepository.findByGroupAndNumber( group, 0L );
        SubGroup subGroup2 = subGroupRepository.findByGroupAndNumber( group, 1L );
        if( subGroup1 == null )
        {
            subGroup1 = new SubGroup();
            subGroup1.setUniversityId( university.getId() );
            subGroup1.setGroup( group );
            subGroup1.setNumber( 0L );
            subGroup1 = subGroupRepository.save( subGroup1 );
        }
        if( subGroup2 == null )
        {
            subGroup2 = new SubGroup();
            subGroup2.setUniversityId( university.getId() );
            subGroup2.setGroup( group );
            subGroup2.setNumber( 1L );
            subGroup2 = subGroupRepository.save( subGroup2 );
        }

        SubGroupLesson subGroupLesson1 = new SubGroupLesson();
        SubGroupLesson subGroupLesson2 = new SubGroupLesson();
        String subGroupNumber = lessonVO.getSubGroupNumber();
        if( subGroupNumber == null || subGroupNumber.equals( "all" ) )
        {
            subGroupLesson1.setSubGroup( subGroup1 );
            subGroupLesson2.setSubGroup( subGroup2 );
            subGroupLesson1.setLesson( lesson );
            subGroupLesson1.setUniversityId( university.getId() );
            subGroupLesson1.setTeacher( teacher );
            subGroupLesson2.setLesson( lesson );
            subGroupLesson2.setTeacher( teacher );
            subGroupLesson2.setUniversityId( university.getId() );
            subGroupLessonRepository.save( subGroupLesson1 );
            subGroupLessonRepository.save( subGroupLesson2 );
        }
        else if( subGroupNumber.equals( "0" ) )
        {
            subGroupLesson1.setSubGroup( subGroup1 );
            subGroupLesson1.setLesson( lesson );
            subGroupLesson1.setTeacher( teacher );
            subGroupLesson1.setUniversityId( university.getId() );
            subGroupLessonRepository.save( subGroupLesson1 );
        }
        else if( subGroupNumber.equals( "1" ) )
        {
            subGroupLesson2.setSubGroup( subGroup2 );
            subGroupLesson2.setLesson( lesson );
            subGroupLesson2.setTeacher( teacher );
            subGroupLesson2.setUniversityId( university.getId() );
            subGroupLessonRepository.save( subGroupLesson2 );
        }
        return group;
    }
}

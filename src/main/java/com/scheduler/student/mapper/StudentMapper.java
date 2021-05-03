package com.scheduler.student.mapper;

import com.scheduler.common.mapper.EntityToVOCrudMapper;
import com.scheduler.student.entity.Student;
import com.scheduler.student.vo.StudentResponseVO;
import com.scheduler.student.vo.StudentUpdateRequestVO;
import com.scheduler.student.vo.StudentCreateRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentMapper implements EntityToVOCrudMapper<Student, StudentCreateRequestVO, StudentUpdateRequestVO, StudentResponseVO>
{
    @Override
    public StudentResponseVO toResponseVO( Student student, Object... args )
    {
        return null;
    }

    @Override
    public Student toNewEntity( StudentCreateRequestVO studentCreateRequestVO, Object... args )
    {
        return null;
    }

    @Override
    public void updateEntity( Student student, StudentUpdateRequestVO studentUpdateRequestVO, Object... args )
    {

    }
}

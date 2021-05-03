package com.scheduler.classroom.mapper;

import com.scheduler.classroom.Classroom;
import com.scheduler.classroom.vo.ClassroomResponseVO;
import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.hull.mapper.HullMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassroomMapper implements EntityToResponseVOMapper<Classroom, ClassroomResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();

    private HullMapper hullMapper;

    public ClassroomMapper( HullMapper hullMapper )
    {
        this.hullMapper = hullMapper;
    }

    @Override
    public ClassroomResponseVO toResponseVO( Classroom classroom, Object... args )
    {
        ClassroomResponseVO classroomResponseVO = modelMapper.map( classroom, ClassroomResponseVO.class );
        classroomResponseVO.setHull( hullMapper.toResponseVO( classroom.getHull() ) );
        return classroomResponseVO;
    }
}

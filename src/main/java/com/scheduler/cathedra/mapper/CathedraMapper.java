package com.scheduler.cathedra.mapper;

import com.scheduler.cathedra.Cathedra;
import com.scheduler.cathedra.vo.CathedraResponseVO;
import com.scheduler.common.mapper.EntityToResponseVOMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CathedraMapper implements EntityToResponseVOMapper<Cathedra, CathedraResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CathedraResponseVO toResponseVO( Cathedra cathedra, Object... args )
    {
        return null;/*modelMapper.map( cathedra, CathedraResponseVO.class );*///todo
    }
}

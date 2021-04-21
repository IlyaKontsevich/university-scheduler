package com.scheduler.hull.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.hull.Hull;
import com.scheduler.hull.vo.HullResponseVO;
import org.springframework.stereotype.Component;

@Component
public class HullMapper implements EntityToResponseVOMapper<Hull, HullResponseVO>
{
    @Override
    public HullResponseVO toResponseVO( Hull hull, Object... args )
    {
        return null;
    }
}

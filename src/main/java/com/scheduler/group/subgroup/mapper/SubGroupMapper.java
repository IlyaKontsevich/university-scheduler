package com.scheduler.group.subgroup.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.group.subgroup.SubGroup;
import com.scheduler.group.subgroup.vo.GroupResponseVO;
import com.scheduler.group.subgroup.vo.SubGroupResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SubGroupMapper implements EntityToResponseVOMapper<SubGroup, SubGroupResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();
    private final GroupMapper groupMapper;

    public SubGroupMapper( GroupMapper groupMapper )
    {
        this.groupMapper = groupMapper;
    }

    @Override
    public SubGroupResponseVO toResponseVO( SubGroup subGroup, Object... args )
    {
        SubGroupResponseVO subGroupResponseVO = modelMapper.map( subGroup, SubGroupResponseVO.class );
        subGroupResponseVO.setGroup( groupMapper.toResponseVO( subGroup.getGroup(), GroupResponseVO.class ) );
        return subGroupResponseVO;
    }
}

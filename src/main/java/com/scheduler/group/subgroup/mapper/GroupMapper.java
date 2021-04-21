package com.scheduler.group.subgroup.mapper;

import com.scheduler.cathedra.mapper.CathedraMapper;
import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.group.Group;
import com.scheduler.group.subgroup.vo.GroupResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper implements EntityToResponseVOMapper<Group, GroupResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();
    private final CathedraMapper cathedraMapper;

    public GroupMapper( CathedraMapper cathedraMapper )
    {
        this.cathedraMapper = cathedraMapper;
    }

    @Override
    public GroupResponseVO toResponseVO( Group group, Object... args )
    {
        GroupResponseVO groupResponseVO = modelMapper.map( group, GroupResponseVO.class );
        groupResponseVO.setCathedra( cathedraMapper.toResponseVO( group.getCathedra() ) );
        return groupResponseVO;
    }
}

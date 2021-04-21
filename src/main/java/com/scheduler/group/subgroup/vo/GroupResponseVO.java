package com.scheduler.group.subgroup.vo;

import com.scheduler.cathedra.vo.CathedraResponseVO;
import lombok.Data;

@Data
public class GroupResponseVO
{
    private Long id;
    private String name;
    private Long universityId;
    private CathedraResponseVO cathedra;
}

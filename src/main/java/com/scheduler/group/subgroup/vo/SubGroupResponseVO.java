package com.scheduler.group.subgroup.vo;

import lombok.Data;

@Data
public class SubGroupResponseVO
{
    private Long id;
    private Long number;
    private Long universityId;
    private GroupResponseVO group;
}

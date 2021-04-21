package com.scheduler.classroom.vo;

import com.scheduler.hull.vo.HullResponseVO;
import lombok.Data;

@Data
public class ClassroomResponseVO
{
    private Long id;
    private String name;
    private Long universityId;
    private HullResponseVO hull;
}

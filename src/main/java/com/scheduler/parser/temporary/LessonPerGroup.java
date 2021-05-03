package com.scheduler.parser.temporary;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LessonPerGroup
{
    private LessonTmp subGroup1;
    private LessonTmp subGroup2;
    private boolean isForAllSubgroup;

    public boolean isEmpty()
    {
        return subGroup1 == null && subGroup2 == null;
    }
}

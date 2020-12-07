package com.scheduler.parser.temporary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class DataPerGroup
{
    @NotNull
    private String groupName;
    @NonNull
    private List<DataPerWeekDay> dataPerWeekDays;
}

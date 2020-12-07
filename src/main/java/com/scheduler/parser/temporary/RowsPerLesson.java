package com.scheduler.parser.temporary;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RowsPerLesson
{
    @NonNull
    private String lessonTime;
    @NotNull
    private List<String> rows;

    public RowsPerLesson( @NonNull String lessonTime, @NotNull List<String> data )
    {
        if( data.size() < 1 )
        {
            throw new RuntimeException( "Invalid lesson row" );
        }
        this.lessonTime = lessonTime;
        this.rows = data;
    }
}

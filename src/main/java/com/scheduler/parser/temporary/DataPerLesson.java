package com.scheduler.parser.temporary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class DataPerLesson
{
    @NonNull
    private String lessonTime;
    @NotNull
    private List<String> row1;
    @NotNull
    private List<String> row2;
    @NotNull
    private List<String> row3;
    @NotNull
    private List<String> row4;

    public void setRow( int number, List<String> splitRow )
    {
        if( number == 0 )
        {
            this.row1 = splitRow;
        }
        if( number == 1 )
        {
            this.row2 = splitRow;
        }
        if( number == 2 )
        {
            this.row3 = splitRow;
        }
        if( number == 3 )
        {
            this.row4 = splitRow;
        }
    }


}

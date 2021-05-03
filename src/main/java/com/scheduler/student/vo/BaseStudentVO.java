package com.scheduler.student.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class BaseStudentVO
{
    /**
     * Student firstName
     */
    @NotBlank
    @Size( min = 1, max = 255 )
    private String name;
    /**
     * Student sex
     */
    @NotBlank
    private String sex;
    /**
     * Student isCapitan
     */
    private boolean isCapitan;
    /**
     * Student dateOfBirth
     */
    @NotNull
    private LocalDateTime dateOfBirth;
    /**
     * Student sub group
     */
    @NotNull
    private Long subGroupId;

    /**
     * Student universityId
     */
    @NotNull
    private Long universityId;

    //todo credentinal
    //todo make separate endpoing for change capitan
}

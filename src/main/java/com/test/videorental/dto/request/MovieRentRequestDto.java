package com.test.videorental.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MovieRentRequestDto extends CommonMovieRequest {

    //annotation values are read at compile time so they can't be kept in the properties file
    // we could create our own validator to omit this restriction but let's just reuse available validators
    @NotNull
    @Min(value = 1)
    @Max(value = 30)
    private Integer nbOfDays;
}

package com.test.videorental.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommonMovieRequest {

    //annotation values are read at compile time so they can't be kept in the properties file
    // we could create our own validator to omit this restriction but let's just reuse available validators

    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    private Long movieId;

    @NotBlank
    @Size(min = 2, max = 255)
    private String movieName;
}

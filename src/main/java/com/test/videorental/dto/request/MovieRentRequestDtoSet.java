package com.test.videorental.dto.request;

import lombok.Data;

import javax.validation.Valid;
import java.util.Set;

@Data
public class MovieRentRequestDtoSet {

    @Valid
    Set<MovieRentRequestDto> movieRentRequestDtoSet;
}

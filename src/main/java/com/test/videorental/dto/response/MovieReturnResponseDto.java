package com.test.videorental.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class MovieReturnResponseDto implements WithErrorMessage {

    private Set<MovieReturnWithSurchargesDto> returnResponse;

    private String errorMessage;

    private BigDecimal totalSurcharges;
}

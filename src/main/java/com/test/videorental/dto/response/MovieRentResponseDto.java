package com.test.videorental.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class MovieRentResponseDto implements WithErrorMessage {

    private Set<MovieRentWithPriceDto> rentResponse;

    private String errorMessage;

    private BigDecimal totalPrice;

}

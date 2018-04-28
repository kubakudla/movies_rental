package com.test.videorental.dto.response;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.enums.VideoType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MovieRentWithPriceDto extends MovieRentRequestDto {

    private VideoType videoType;

    private BigDecimal price;
}

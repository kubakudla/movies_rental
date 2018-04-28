package com.test.videorental.dto.response;

import com.test.videorental.dto.request.CommonMovieRequest;
import com.test.videorental.enums.VideoType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MovieReturnWithSurchargesDto extends CommonMovieRequest {

    private VideoType videoType;

    private Integer nbOfDaysLate;

    private BigDecimal surcharges;
}

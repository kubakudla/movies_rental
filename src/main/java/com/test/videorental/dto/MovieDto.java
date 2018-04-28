package com.test.videorental.dto;

import com.test.videorental.enums.VideoType;
import lombok.Data;

@Data
public class MovieDto {

    private String name;

    private VideoType videoType;
}

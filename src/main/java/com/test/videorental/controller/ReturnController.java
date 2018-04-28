package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.service.ReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/return/{customerId}")
public class ReturnController {

    private final ReturnService returnService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MovieReturnResponseDto> returnMovies(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieReturnRequestDtoSet rentReturnRequestDtoList) {
        logger.info("Running 'returnMovies' endpoint for customerId: " + customerId);
        logger.info("Accepting movieReturnRequestDtos: " + rentReturnRequestDtoList);
        ResponseEntity<MovieReturnResponseDto> response = returnService.returnMovies(customerId, rentReturnRequestDtoList);
        logger.info("Sending response: " + response);
        return response;
    }
}

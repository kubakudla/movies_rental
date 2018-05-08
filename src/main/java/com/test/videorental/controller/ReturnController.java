package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.service.ReturnService;
import com.test.videorental.validator.ReturnValidator;
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
import java.util.Set;

@RestController
@RequestMapping("/api/return/{customerId}")
public class ReturnController {

    private final ReturnValidator returnValidator;

    private final ReturnService returnService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReturnController(ReturnValidator returnValidator, ReturnService returnService) {
        this.returnValidator = returnValidator;
        this.returnService = returnService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MovieReturnResponseDto> returnMovies(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieReturnRequestDtoSet rentReturnRequestDtoSet) {
        logger.info("Running 'returnMovies' endpoint for customerId: " + customerId);

        logger.info("Validating movieReturnRequestDtos: " + rentReturnRequestDtoSet);
        ResponseEntity<MovieReturnResponseDto> validationResponse = validateMovieReturn(customerId, rentReturnRequestDtoSet);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Failed to validate movieReturnRequestDtos: " + validationResponse.getBody());
            return validationResponse;
        }

        ResponseEntity<MovieReturnResponseDto> response = returnService.returnMovies(customerId, rentReturnRequestDtoSet);
        logger.info("Sending response: " + response);
        return response;
    }

    private ResponseEntity<MovieReturnResponseDto> validateMovieReturn(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieReturnRequestDtoSet rentReturnRequestDtoSet) {
        Set<MovieReturnRequestDto> movieReturnRequestDtos = rentReturnRequestDtoSet.getMovieReturnRequestDtoSet();
        return returnValidator.validateMovieReturn(customerId, movieReturnRequestDtos);
    }
}
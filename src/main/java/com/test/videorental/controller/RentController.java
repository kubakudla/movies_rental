package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.dto.response.MovieRentResponseDto;
import com.test.videorental.service.RentService;
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
@RequestMapping("/api/rental/{customerId}")
public class RentController {

    private final RentService rentService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MovieRentResponseDto> rentMovies(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieRentRequestDtoSet rentRequestDtoList) {
        logger.info("Running 'returnMovies' endpoint for customerId: " + customerId);
        logger.info("Accepting movieRentRequestDtos: " + rentRequestDtoList);
        ResponseEntity<MovieRentResponseDto> response = rentService.rentMovies(customerId, rentRequestDtoList);
        logger.info("Sending response: " + response);
        return response;
    }
}

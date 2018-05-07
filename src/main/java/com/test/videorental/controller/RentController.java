package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.dto.response.MovieRentResponseDto;
import com.test.videorental.service.RentService;
import com.test.videorental.validator.RentValidator;
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
@RequestMapping("/api/rental/{customerId}")
public class RentController {

    private final RentValidator rentValidator;

    private final RentService rentService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RentController(RentValidator rentValidator, RentService rentService) {
        this.rentValidator = rentValidator;
        this.rentService = rentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MovieRentResponseDto> rentMovies(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieRentRequestDtoSet rentRequestDtoSet) {
        logger.info("Running 'returnMovies' endpoint for customerId: " + customerId);

        logger.info("Validating movieRentRequestDtos: " + rentRequestDtoSet);
        ResponseEntity<MovieRentResponseDto> validationResponse = validateMovieRequest(customerId, rentRequestDtoSet);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Failed to validate movieRentRequestDtos:" + validationResponse.getBody() );
            return validationResponse;
        }

        ResponseEntity<MovieRentResponseDto> response = rentService.rentMovies(customerId, rentRequestDtoSet);
        logger.info("Sending response: " + response);
        return response;
    }

    private ResponseEntity<MovieRentResponseDto> validateMovieRequest(@PathVariable("customerId") long customerId, @Valid @RequestBody MovieRentRequestDtoSet rentRequestDtoSet) {
        Set<MovieRentRequestDto> movieRentRequestDtos = rentRequestDtoSet.getMovieRentRequestDtoSet();
        MovieRentResponseDto responseDto = new MovieRentResponseDto();
        return rentValidator.validateMovieRequest(customerId, movieRentRequestDtos, responseDto);
    }
}
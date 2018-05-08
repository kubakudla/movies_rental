package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.entity.Rental;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReturnValidator extends CommonValidator {

    private final RentalRepository rentalRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReturnValidator(CustomerRepository customerRepository, MovieRepository movieRepository, RentalRepository rentalRepository) {
        super(customerRepository, movieRepository);
        this.rentalRepository = rentalRepository;
    }

    public ResponseEntity<MovieReturnResponseDto> validateMovieReturn(long customerId, Set<MovieReturnRequestDto> movieReturnRequestDtos) {
        MovieReturnResponseDto responseDto = new MovieReturnResponseDto();
        if (!isValid(customerId, movieReturnRequestDtos, responseDto)) {
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    boolean isValid(long customerId, Set<MovieReturnRequestDto> movieReturnRequestDtos, MovieReturnResponseDto responseDto) {
        return customerValid(customerId, responseDto)
            && moviesDataValid(movieReturnRequestDtos, responseDto)
            && rentalValid(customerId, movieReturnRequestDtos, responseDto);
    }

    boolean rentalValid(long customerId, Set<MovieReturnRequestDto> movieReturnRequestDtos, MovieReturnResponseDto responseDto) {
        Set<MovieReturnRequestDto> wrongRentals = findWrongRentals(customerId, movieReturnRequestDtos);
        if (!wrongRentals.isEmpty()) {
            responseDto.setErrorMessage("Wrong return requests: " + wrongRentals);
            logger.debug("Wrong return requests: " + wrongRentals);
            return false;
        }
        return true;
    }

    private Set<MovieReturnRequestDto> findWrongRentals(long customerId, Set<MovieReturnRequestDto> movieReturnRequestDtos) {
        Set<Rental> rentalList = movieReturnRequestDtos.stream().map(m -> rentalRepository.findActiveRental(customerId, m.getMovieId())).collect(Collectors.toSet());
        if (!rentalList.isEmpty()) {
            Set<Long> ids = rentalList.stream().filter(Objects::nonNull).map(r -> r.getMovie().getId()).collect(Collectors.toSet());
            return movieReturnRequestDtos.stream().filter(m -> !ids.contains(m.getMovieId())).collect(Collectors.toSet());
        }
        return movieReturnRequestDtos;
    }
}

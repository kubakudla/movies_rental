package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.response.MovieRentResponseDto;
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
public class RentValidator extends CommonValidator {

    private final RentalRepository rentalRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RentValidator(CustomerRepository customerRepository, MovieRepository movieRepository, RentalRepository rentalRepository) {
        super(customerRepository, movieRepository);
        this.rentalRepository = rentalRepository;
    }

    public ResponseEntity<MovieRentResponseDto> validateMovieRequest(long customerId, Set<MovieRentRequestDto> movieRentRequestDtos) {
        MovieRentResponseDto responseDto = new MovieRentResponseDto();
        if (!isValid(customerId, movieRentRequestDtos, responseDto)) {
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    boolean isValid(long customerId, Set<MovieRentRequestDto> movieRentRequestDtos, MovieRentResponseDto responseDto) {
        return customerValid(customerId, responseDto)
            && moviesDataValid(movieRentRequestDtos, responseDto)
            && movieAvailableValid(movieRentRequestDtos, responseDto);
    }

    boolean movieAvailableValid(Set<MovieRentRequestDto> movieRentRequestDtos, MovieRentResponseDto responseDto) {
        Set<MovieRentRequestDto> wrongRentals = findWrongRentals(movieRentRequestDtos);
        if (!wrongRentals.isEmpty()) {
            responseDto.setErrorMessage("Movie already rented: " + wrongRentals);
            logger.debug("Movie already rented: " + wrongRentals);
            return false;
        }
        return true;
    }

    private Set<MovieRentRequestDto> findWrongRentals(Set<MovieRentRequestDto> movieReturnRequestDtos) {
        Set<Rental> rentalList = movieReturnRequestDtos.stream().map(m -> rentalRepository.findActiveRental(m.getMovieId())).collect(Collectors.toSet());
        if (!rentalList.isEmpty()) {
            Set<Long> ids = rentalList.stream().filter(Objects::nonNull).map(r -> r.getMovie().getId()).collect(Collectors.toSet());
            return movieReturnRequestDtos.stream().filter(m -> ids.contains(m.getMovieId())).collect(Collectors.toSet());
        }
        return movieReturnRequestDtos;
    }
}
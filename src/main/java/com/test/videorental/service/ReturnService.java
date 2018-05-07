package com.test.videorental.service;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.dto.response.MovieReturnWithSurchargesDto;
import com.test.videorental.entity.Rental;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import com.test.videorental.validator.ReturnValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReturnService {

    private final CalculationService calculationService;

    private final RentalRepository rentalRepository;

    private final MovieRepository movieRepository;

    private final ModelMapper mapper;

    @Autowired
    public ReturnService(CalculationService calculationService,  RentalRepository rentalRepository,
                         MovieRepository movieRepository, ModelMapper mapper) {
        this.calculationService = calculationService;
        this.rentalRepository = rentalRepository;
        this.movieRepository = movieRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<MovieReturnResponseDto> returnMovies(long customerId, MovieReturnRequestDtoSet rentReturnRequestDtoList) {
        Set<MovieReturnRequestDto> movieReturnRequestDtos = rentReturnRequestDtoList.getMovieReturnRequestDtoSet();

        Set<MovieReturnWithSurchargesDto> rentResponse = prepareReturnResponse(customerId, movieReturnRequestDtos);
        updateRentals(customerId, rentResponse);

        MovieReturnResponseDto responseDto = initResponseDto(rentResponse);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private Set<MovieReturnWithSurchargesDto> prepareReturnResponse(long customerId, Set<MovieReturnRequestDto> movieReturnRequestDtos) {
        Set<MovieReturnWithSurchargesDto> returnResponse = mapRequestToResponse(movieReturnRequestDtos);
        updateMovieType(returnResponse);
        calculateSurcharges(customerId, returnResponse);
        returnResponse.stream().sorted(Comparator.comparing(MovieReturnWithSurchargesDto::getMovieId));
        return returnResponse;
    }

    @Transactional
    void updateRentals(long customerId, Set<MovieReturnWithSurchargesDto> rentResponse) {
        Set<Rental> rentList = rentResponse.stream().map(r -> updateRental(customerId, r)).collect(Collectors.toSet());
        rentalRepository.save(rentList);
    }

    private MovieReturnResponseDto initResponseDto(Set<MovieReturnWithSurchargesDto> rentResponse) {
        MovieReturnResponseDto responseDto = new MovieReturnResponseDto();
        responseDto.setReturnResponse(rentResponse);
        responseDto.setErrorMessage("");
        responseDto.setTotalSurcharges(rentResponse.stream().map(r -> r.getSurcharges()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return responseDto;
    }

    private Set<MovieReturnWithSurchargesDto> mapRequestToResponse(Set<MovieReturnRequestDto> movieReturnRequestDtos) {
        return movieReturnRequestDtos.stream().map(m -> mapper.map(m, MovieReturnWithSurchargesDto.class)).collect(Collectors.toSet());
    }

    private void updateMovieType(Set<MovieReturnWithSurchargesDto> rentResponse) {
        rentResponse.stream().forEach(m -> m.setVideoType(movieRepository.findVideoTypeById(m.getMovieId())));
    }

    private void calculateSurcharges(long customerId, Set<MovieReturnWithSurchargesDto> rentResponse) {
        rentResponse.stream().forEach(m -> m.setNbOfDaysLate(calculationService.calculateDaysLate(customerId, m.getMovieId())));
        rentResponse.stream().forEach(m -> m.setSurcharges(calculationService.calculateSurcharges(customerId, m.getMovieId())));
    }

    private Rental updateRental(long customerId, MovieReturnWithSurchargesDto movieReturnWithSurchargesDto) {
        Rental rental = rentalRepository.findActiveRental(customerId, movieReturnWithSurchargesDto.getMovieId());
        rental.setReturnDate(LocalDate.now());
        rental.setNbOfDaysLate(movieReturnWithSurchargesDto.getNbOfDaysLate());
        rental.setSurcharges(movieReturnWithSurchargesDto.getSurcharges());
        return rental;
    }
}
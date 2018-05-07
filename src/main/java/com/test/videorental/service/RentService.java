package com.test.videorental.service;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.dto.response.MovieRentResponseDto;
import com.test.videorental.dto.response.MovieRentWithPriceDto;
import com.test.videorental.entity.Rental;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import com.test.videorental.validator.RentValidator;
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
public class RentService {

    private final CalculationService calculationService;

    private final BonusPointService bonusPointService;



    private final CustomerRepository customerRepository;

    private final MovieRepository movieRepository;

    private final RentalRepository rentalRepository;

    private final ModelMapper mapper;

    @Autowired
    public RentService(CalculationService calculationService, BonusPointService bonusPointService, RentValidator rentValidator,
                       RentalRepository rentalRepository, CustomerRepository customerRepository,
                       MovieRepository movieRepository, ModelMapper mapper) {
        this.calculationService = calculationService;
        this.bonusPointService = bonusPointService;
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.rentalRepository = rentalRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<MovieRentResponseDto> rentMovies(long customerId, MovieRentRequestDtoSet movieRentRequestDtoSet) {
        Set<MovieRentRequestDto> movieRentRequestDtos = movieRentRequestDtoSet.getMovieRentRequestDtoSet();

        Set<MovieRentWithPriceDto> rentResponse = prepareRentResponse(movieRentRequestDtos);
        Set<Rental> rentalList = saveRentals(customerId, rentResponse);

        MovieRentResponseDto responseDto = initResponseDto(rentResponse, rentalList);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private Set<MovieRentWithPriceDto> prepareRentResponse(Set<MovieRentRequestDto> movieRentRequestDtos) {
        Set<MovieRentWithPriceDto> rentResponse = mapRequestToResponse(movieRentRequestDtos);
        updateMovieType(rentResponse);
        rentResponse.stream().sorted(Comparator.comparing(MovieRentWithPriceDto::getMovieId));
        return rentResponse;
    }

    @Transactional
    Set<Rental> saveRentals(long customerId, Set<MovieRentWithPriceDto> rentResponse) {
        Set<Rental> rentalList = rentResponse.stream().map(r -> createNewRentAndCalculatePrice(customerId, r)).collect(Collectors.toSet());
        rentalRepository.save(rentalList);
        bonusPointService.addBonusPoints(customerId, rentalList);
        return rentalList;
    }

    private MovieRentResponseDto initResponseDto(Set<MovieRentWithPriceDto> rentResponse, Set<Rental> rentalList) {
        MovieRentResponseDto responseDto = new MovieRentResponseDto();
        responseDto.setRentResponse(rentResponse);
        responseDto.setErrorMessage("");
        responseDto.setTotalPrice(rentalList.stream().map(r -> r.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return responseDto;
    }

    private Set<MovieRentWithPriceDto> mapRequestToResponse(Set<MovieRentRequestDto> movieRentRequestDtos) {
        return movieRentRequestDtos.stream().map(m -> mapper.map(m, MovieRentWithPriceDto.class)).collect(Collectors.toSet());
    }

    private void updateMovieType(Set<MovieRentWithPriceDto> rentResponse) {
        rentResponse.stream().forEach(m -> m.setVideoType(movieRepository.findVideoTypeById(m.getMovieId())));
    }

    private Rental createNewRentAndCalculatePrice(long customerId, MovieRentWithPriceDto movieRentWithPriceDto) {
        BigDecimal price = calculatePrice(movieRentWithPriceDto);
        movieRentWithPriceDto.setPrice(price);

        Rental rental = new Rental();
        rental.setCustomer(customerRepository.findOne(customerId));
        rental.setMovie(movieRepository.findOne(movieRentWithPriceDto.getMovieId()));
        rental.setNbOfDays(movieRentWithPriceDto.getNbOfDays());
        rental.setRentDate(LocalDate.now());
        rental.setPrice(price);
        return rental;
    }

    private BigDecimal calculatePrice(MovieRentWithPriceDto movieRentWithPriceDto) {
        return calculationService.calculatePrice(movieRentWithPriceDto.getMovieId(), movieRentWithPriceDto.getNbOfDays());
    }
}
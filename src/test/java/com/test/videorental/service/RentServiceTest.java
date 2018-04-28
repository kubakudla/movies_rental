package com.test.videorental.service;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.dto.response.MovieRentResponseDto;
import com.test.videorental.dto.response.MovieRentWithPriceDto;
import com.test.videorental.enums.VideoType;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import com.test.videorental.validator.RentValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Set;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_NAME;
import static com.test.videorental.validator.ValidationUtil.NB_OF_DAYS;
import static com.test.videorental.validator.ValidationUtil.initMovieRentRequest;
import static com.test.videorental.validator.ValidationUtil.initMovieRentWithPriceDto;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RentServiceTest {

    @InjectMocks
    private RentService rentService;

    @Mock
    private RentValidator rentValidator;

    @Mock
    private CalculationService calculationService;

    @Mock
    private BonusPointService bonusPointService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void test_rentMovies_success() {
        //given
        MovieRentRequestDtoSet movieRentRequestDtoSet = initMovieRentRequest();
        VideoType videoType = VideoType.NEW_RELEASE;
        BigDecimal price = BigDecimal.TEN;

        //when
        when(mapper.map(any(), any())).thenReturn(initMovieRentWithPriceDto(videoType));
        when(movieRepository.findVideoTypeById(MOVIE_ID)).thenReturn(videoType);
        when(calculationService.calculatePrice(MOVIE_ID, NB_OF_DAYS)).thenReturn(price);
        ResponseEntity<MovieRentResponseDto> movieRentResponseDtoResponseEntity = rentService.rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);

        //then
        MovieRentResponseDto movieRentResponseDto = movieRentResponseDtoResponseEntity.getBody();
        Set<MovieRentWithPriceDto> movieRentWithPriceDtos = movieRentResponseDto.getRentResponse();
        MovieRentWithPriceDto rentWithPriceDto = movieRentWithPriceDtos.iterator().next();

        assertEquals(HttpStatus.OK, movieRentResponseDtoResponseEntity.getStatusCode());
        assertEquals("", movieRentResponseDto.getErrorMessage());
        assertEquals(price, movieRentResponseDto.getTotalPrice());
        assertEquals(1, movieRentWithPriceDtos.size());
        assertEquals(NB_OF_DAYS, rentWithPriceDto.getNbOfDays());
        assertEquals(videoType, rentWithPriceDto.getVideoType());
        assertEquals(price, rentWithPriceDto.getPrice());
        assertEquals(MOVIE_ID, rentWithPriceDto.getMovieId());
        assertEquals(MOVIE_NAME, rentWithPriceDto.getMovieName());
    }

    @Test
    public void test_rentTwoMovies_success() {
        //given
        MovieRentRequestDtoSet movieRentRequestDtoSet = initMovieRentRequest(2);
        VideoType videoType = VideoType.NEW_RELEASE;
        BigDecimal price = BigDecimal.TEN;
        BigDecimal price2 = new BigDecimal(40);
        Long MOVIE_ID_2 = MOVIE_ID + 1;
        String MOVIE_NAME_2 = MOVIE_NAME + "1";

        MovieRentRequestDto movieRentRequestDto1 = movieRentRequestDtoSet.getMovieRentRequestDtoSet().stream().filter(m -> m.getMovieId().equals(1l)).findFirst().get();
        MovieRentRequestDto movieRentRequestDto2 = movieRentRequestDtoSet.getMovieRentRequestDtoSet().stream().filter(m -> m.getMovieId().equals(2l)).findFirst().get();

        //when
        when(mapper.map(movieRentRequestDto1, MovieRentWithPriceDto.class)).thenReturn(initMovieRentWithPriceDto(videoType));
        when(mapper.map(movieRentRequestDto2, MovieRentWithPriceDto.class)).thenReturn(initMovieRentWithPriceDto(videoType, 2));
        when(movieRepository.findVideoTypeById(MOVIE_ID)).thenReturn(videoType);
        when(movieRepository.findVideoTypeById(MOVIE_ID_2)).thenReturn(videoType);
        when(calculationService.calculatePrice(MOVIE_ID, NB_OF_DAYS)).thenReturn(price);
        when(calculationService.calculatePrice(MOVIE_ID_2, NB_OF_DAYS)).thenReturn(price2);
        ResponseEntity<MovieRentResponseDto> movieRentResponseDtoResponseEntity = rentService.rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);

        //then
        MovieRentResponseDto movieRentResponseDto = movieRentResponseDtoResponseEntity.getBody();
        Set<MovieRentWithPriceDto> movieRentWithPriceDtos = movieRentResponseDto.getRentResponse();
        MovieRentWithPriceDto rentWithPriceDto1 = movieRentWithPriceDtos.stream().filter(m -> m.getMovieId().equals(1l)).findFirst().get();
        MovieRentWithPriceDto rentWithPriceDto2 = movieRentWithPriceDtos.stream().filter(m -> m.getMovieId().equals(2l)).findFirst().get();

        assertEquals(HttpStatus.OK, movieRentResponseDtoResponseEntity.getStatusCode());
        assertEquals("", movieRentResponseDto.getErrorMessage());
        assertEquals(price.add(price2), movieRentResponseDto.getTotalPrice());
        assertEquals(2, movieRentWithPriceDtos.size());

        assertEquals(NB_OF_DAYS, rentWithPriceDto1.getNbOfDays());
        assertEquals(videoType, rentWithPriceDto1.getVideoType());
        assertEquals(price, rentWithPriceDto1.getPrice());
        assertEquals(MOVIE_ID, rentWithPriceDto1.getMovieId());
        assertEquals(MOVIE_NAME, rentWithPriceDto1.getMovieName());

        assertEquals(NB_OF_DAYS, rentWithPriceDto2.getNbOfDays());
        assertEquals(videoType, rentWithPriceDto2.getVideoType());
        assertEquals(price2, rentWithPriceDto2.getPrice());
        assertEquals(MOVIE_ID_2, rentWithPriceDto2.getMovieId());
        assertEquals(MOVIE_NAME_2, rentWithPriceDto2.getMovieName());
    }
}
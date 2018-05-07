package com.test.videorental.service;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.dto.response.MovieReturnWithSurchargesDto;
import com.test.videorental.enums.VideoType;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import com.test.videorental.validator.ReturnValidator;
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
import static com.test.videorental.validator.ValidationUtil.initMovieReturnRequest;
import static com.test.videorental.validator.ValidationUtil.initMovieReturnWithSurcharges;
import static com.test.videorental.validator.ValidationUtil.initRental;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnServiceTest {

    @InjectMocks
    private ReturnService returnService;

    @Mock
    private CalculationService calculationService;

    @SuppressWarnings("unused")
    @Mock
    private ReturnValidator returnValidator;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void test_returnMovies_success() {
        //given
        MovieReturnRequestDtoSet movieReturnRequestDtoSet = initMovieReturnRequest();
        VideoType videoType = VideoType.NEW_RELEASE;
        BigDecimal price = BigDecimal.TEN;

        //when
        when(mapper.map(any(), any())).thenReturn(initMovieReturnWithSurcharges(videoType));
        when(calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID)).thenReturn(price);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(initRental());
        when(movieRepository.findVideoTypeById(MOVIE_ID)).thenReturn(videoType);
        ResponseEntity<MovieReturnResponseDto> movieReturnResponseDtoResponseEntity = returnService.returnMovies(CUSTOMER_ID, movieReturnRequestDtoSet);

        //then
        MovieReturnResponseDto movieReturnResponseDto = movieReturnResponseDtoResponseEntity.getBody();
        Set<MovieReturnWithSurchargesDto> movieReturnWithSurchargesDtos = movieReturnResponseDto.getReturnResponse();
        MovieReturnWithSurchargesDto returnWithSurchargesDto = movieReturnWithSurchargesDtos.iterator().next();

        assertEquals(HttpStatus.OK, movieReturnResponseDtoResponseEntity.getStatusCode());
        assertEquals("", movieReturnResponseDto.getErrorMessage());
        assertEquals(price, movieReturnResponseDto.getTotalSurcharges());
        assertEquals(1, movieReturnWithSurchargesDtos.size());
        assertEquals(videoType, returnWithSurchargesDto.getVideoType());
        assertEquals(price, returnWithSurchargesDto.getSurcharges());
        assertEquals(MOVIE_ID, returnWithSurchargesDto.getMovieId());
        assertEquals(MOVIE_NAME, returnWithSurchargesDto.getMovieName());
    }

    @Test
    public void test_returnTwoMovies_success() {
        //given
        MovieReturnRequestDtoSet movieReturnRequestDtoSet = initMovieReturnRequest(2);
        VideoType videoType = VideoType.NEW_RELEASE;
        BigDecimal surcharges = BigDecimal.TEN;
        BigDecimal surcharges2 = new BigDecimal(40);
        Long MOVIE_ID_2 = MOVIE_ID + 1;
        String MOVIE_NAME_2 = MOVIE_NAME + "1";

        MovieReturnRequestDto movieReturnRequestDto1 = movieReturnRequestDtoSet.getMovieReturnRequestDtoSet().stream().filter(m -> m.getMovieId().equals(1l)).findFirst().get();
        MovieReturnRequestDto movieReturnRequestDto2 = movieReturnRequestDtoSet.getMovieReturnRequestDtoSet().stream().filter(m -> m.getMovieId().equals(2l)).findFirst().get();

        //when
        when(mapper.map(movieReturnRequestDto1, MovieReturnWithSurchargesDto.class)).thenReturn(initMovieReturnWithSurcharges(videoType));
        when(mapper.map(movieReturnRequestDto2, MovieReturnWithSurchargesDto.class)).thenReturn(initMovieReturnWithSurcharges(videoType, 2));
        when(movieRepository.findVideoTypeById(MOVIE_ID)).thenReturn(videoType);
        when(movieRepository.findVideoTypeById(MOVIE_ID_2)).thenReturn(videoType);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(initRental());
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID_2)).thenReturn(initRental());
        when(calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID)).thenReturn(surcharges);
        when(calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID_2)).thenReturn(surcharges2);
        ResponseEntity<MovieReturnResponseDto> movieReturnResponseDtoResponseEntity = returnService.returnMovies(CUSTOMER_ID, movieReturnRequestDtoSet);

        //then
        MovieReturnResponseDto movieRentResponseDto = movieReturnResponseDtoResponseEntity.getBody();
        Set<MovieReturnWithSurchargesDto> movieRentWithPriceDtos = movieRentResponseDto.getReturnResponse();
        MovieReturnWithSurchargesDto rentWithPriceDto1 = movieRentWithPriceDtos.stream().filter(m -> m.getMovieId().equals(1l)).findFirst().get();
        MovieReturnWithSurchargesDto rentWithPriceDto2 = movieRentWithPriceDtos.stream().filter(m -> m.getMovieId().equals(2l)).findFirst().get();

        assertEquals(HttpStatus.OK, movieReturnResponseDtoResponseEntity.getStatusCode());
        assertEquals("", movieRentResponseDto.getErrorMessage());
        assertEquals(surcharges.add(surcharges2), movieRentResponseDto.getTotalSurcharges());
        assertEquals(2, movieRentWithPriceDtos.size());

        assertEquals(videoType, rentWithPriceDto1.getVideoType());
        assertEquals(surcharges, rentWithPriceDto1.getSurcharges());
        assertEquals(MOVIE_ID, rentWithPriceDto1.getMovieId());
        assertEquals(MOVIE_NAME, rentWithPriceDto1.getMovieName());

        assertEquals(videoType, rentWithPriceDto2.getVideoType());
        assertEquals(surcharges2, rentWithPriceDto2.getSurcharges());
        assertEquals(MOVIE_ID_2, rentWithPriceDto2.getMovieId());
        assertEquals(MOVIE_NAME_2, rentWithPriceDto2.getMovieName());
    }
}

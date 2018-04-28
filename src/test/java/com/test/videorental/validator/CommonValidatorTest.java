package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.response.MovieRentResponseDto;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.entity.Movie;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_NAME;
import static com.test.videorental.validator.ValidationUtil.initMovie;
import static com.test.videorental.validator.ValidationUtil.initMovieRentRequestDtoList;
import static com.test.videorental.validator.ValidationUtil.initMovieReturnRequestDtoList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommonValidatorTest {

    @InjectMocks
    private CommonValidator commonValidator;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Test
    public void test_customerValid_success() {
        //given
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        boolean customerValid = commonValidator.customerValid(CUSTOMER_ID, movieRentResponseDto);

        //then
        assertTrue(customerValid);
    }

    @Test
    public void test_customerValid_fail() {
        //given
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(false);
        boolean customerValid = commonValidator.customerValid(CUSTOMER_ID, movieRentResponseDto);

        //then
        assertFalse(customerValid);
    }

    @Test
    public void test_moviesRentDataValid_success() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        Movie movie = initMovie();

        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        boolean moviesDataValid = commonValidator.moviesDataValid(movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertTrue(moviesDataValid);
    }

    @Test
    public void test_moviesRentDataValid_fail() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();

        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(null);
        boolean moviesDataValid = commonValidator.moviesDataValid(movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertFalse(moviesDataValid);
    }

    @Test
    public void test_moviesReturnDataValid_success() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();

        Movie movie = initMovie();

        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();

        //when
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        boolean moviesDataValid = commonValidator.moviesDataValid(movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertTrue(moviesDataValid);
    }

    @Test
    public void test_moviesReturnDataValid_fail() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();

        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();

        //when
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(null);
        boolean moviesDataValid = commonValidator.moviesDataValid(movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertFalse(moviesDataValid);
    }
}
package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.response.MovieReturnResponseDto;
import com.test.videorental.entity.Movie;
import com.test.videorental.entity.Rental;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
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
import static com.test.videorental.validator.ValidationUtil.initMovieReturnRequestDtoList;
import static com.test.videorental.validator.ValidationUtil.initRental;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnValidatorTest {

    @InjectMocks
    private ReturnValidator returnValidator;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Test
    public void test_rentalValid_success() {
        // given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();
        Rental rental = initRental();

        //when
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        boolean rentalValid = returnValidator.rentalValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertTrue(rentalValid);
    }

    @Test
    public void test_rentalValid_failure() {
        // given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();

        //when
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(null);
        boolean rentalValid = returnValidator.rentalValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertFalse(rentalValid);
    }

    @Test
    public void test_isValid_success() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();
        Movie movie = initMovie();
        Rental rental = initRental();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        boolean isValid = returnValidator.isValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertTrue(isValid);
    }

    @Test
    public void test_isValid_failCustomerNotValid() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();
        Movie movie = initMovie();
        Rental rental = initRental();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(false);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        boolean isValid = returnValidator.isValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertFalse(isValid);
    }

    @Test
    public void test_isValid_failMoviesDataNotValid() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();
        Movie movie = initMovie();
        Rental rental = initRental();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(null);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        boolean isValid = returnValidator.isValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertFalse(isValid);
    }

    @Test
    public void test_isValid_failRentalNotValid() {
        //given
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = initMovieReturnRequestDtoList();
        MovieReturnResponseDto movieReturnResponseDto = new MovieReturnResponseDto();
        Movie movie = initMovie();
        Rental rental = initRental();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(null);
        boolean isValid = returnValidator.isValid(CUSTOMER_ID, movieReturnRequestDtoList, movieReturnResponseDto);

        //then
        assertFalse(isValid);
    }
}

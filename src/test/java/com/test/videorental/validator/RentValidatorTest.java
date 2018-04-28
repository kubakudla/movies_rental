package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.response.MovieRentResponseDto;
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
import static com.test.videorental.validator.ValidationUtil.initMovieRentRequestDtoList;
import static com.test.videorental.validator.ValidationUtil.initRental;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RentValidatorTest {

    @InjectMocks
    private RentValidator rentValidator;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Test
    public void test_movieAvailableValid_success() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(null);
        boolean movieAvailableValid = rentValidator.movieAvailableValid(movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertTrue(movieAvailableValid);
    }

    @Test
    public void test_movieAvailableValid_failure() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();
        Rental rental = initRental();

        //when
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(rental);
        boolean movieAvailableValid = rentValidator.movieAvailableValid(movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertFalse(movieAvailableValid);
    }

    @Test
    public void test_isValid_success() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();
        Movie movie = initMovie();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(null);
        boolean isValid = rentValidator.isValid(CUSTOMER_ID, movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertTrue(isValid);
    }

    @Test
    public void test_isValid_failCustomerNotValid() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();
        Movie movie = initMovie();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(false);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(null);
        boolean isValid = rentValidator.isValid(CUSTOMER_ID, movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertFalse(isValid);
    }

    @Test
    public void test_isValid_failMoviesDataNotValid() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(null);
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(null);
        boolean isValid = rentValidator.isValid(CUSTOMER_ID, movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertFalse(isValid);
    }

    @Test
    public void test_isValid_failMovieAvailableValid() {
        //given
        Set<MovieRentRequestDto> movieRentRequestDtoList = initMovieRentRequestDtoList();
        MovieRentResponseDto movieRentResponseDto = new MovieRentResponseDto();
        Movie movie = initMovie();
        Rental rental = initRental();

        //when
        when(customerRepository.exists(CUSTOMER_ID)).thenReturn(true);
        when(movieRepository.findByNameAndId(MOVIE_NAME, MOVIE_ID)).thenReturn(movie);
        when(rentalRepository.findActiveRental(MOVIE_ID)).thenReturn(rental);
        boolean isValid = rentValidator.isValid(CUSTOMER_ID, movieRentRequestDtoList, movieRentResponseDto);

        //then
        assertFalse(isValid);
    }
}

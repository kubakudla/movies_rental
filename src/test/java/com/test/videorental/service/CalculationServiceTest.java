package com.test.videorental.service;

import com.test.videorental.entity.Movie;
import com.test.videorental.entity.Rental;
import com.test.videorental.enums.VideoType;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Before
    public void init() {
        ReflectionTestUtils.setField(calculationService, "PRICE_PREMIUM", "40");
        ReflectionTestUtils.setField(calculationService, "PRICE_BASIC", "30");
        ReflectionTestUtils.setField(calculationService, "REGULAR_TYPE_PRICE_LIMIT", "3");
        ReflectionTestUtils.setField(calculationService, "OLD_TYPE_PRICE_LIMIT", "5");
    }

    @Test
    public void test_calculatePrice_newOrder1Day() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.NEW_RELEASE);
        int days = 1;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("40"), price);
    }

    @Test
    public void test_calculatePrice_newOrder10Days() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.NEW_RELEASE);
        int days = 10;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("400"), price);
    }

    @Test
    public void test_calculatePrice_regularOrder1Day() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.REGULAR);
        int days = 1;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("30"), price);
    }

    @Test
    public void test_calculatePrice_regularOrder3Days() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.REGULAR);
        int days = 3;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("30"), price);
    }

    @Test
    public void test_calculatePrice_regularOrder10Days() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.REGULAR);
        int days = 10;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("240"), price);
    }

    @Test
    public void test_calculatePrice_oldOrder1Day() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.OLD);
        int days = 1;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("30"), price);
    }

    @Test
    public void test_calculatePrice_oldOrder5Days() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.OLD);
        int days = 5;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("30"), price);
    }

    @Test
    public void test_calculatePrice_oldOrder10Days() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.OLD);
        int days = 10;

        //when
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal price = calculationService.calculatePrice(MOVIE_ID, days);

        //then
        assertEquals(new BigDecimal("180"), price);
    }

    @Test
    public void test_calculateSurcharges_newRelease() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.NEW_RELEASE);

        Rental rental = initRental(5, 12);

        //when
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal surcharges = calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID);

        //then
        assertEquals(new BigDecimal("280"), surcharges);
    }

    @Test
    public void test_calculateSurcharges_regular() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.REGULAR);

        Rental rental = initRental(4, 17);

        //when
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal surcharges = calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID);

        //then
        assertEquals(new BigDecimal("390"), surcharges);
    }

    @Test
    public void test_calculateSurcharges_Old() {
        //given
        Movie movie = new Movie();
        movie.setVideoType(VideoType.OLD);

        Rental rental = initRental(6, 12);

        //when
        when(rentalRepository.findActiveRental(CUSTOMER_ID, MOVIE_ID)).thenReturn(rental);
        when(movieRepository.findOne(MOVIE_ID)).thenReturn(movie);
        BigDecimal surcharges = calculationService.calculateSurcharges(CUSTOMER_ID, MOVIE_ID);

        //then
        assertEquals(new BigDecimal("180"), surcharges);
    }

    private Rental initRental(int nbOfDays, int howManyDaysAgoRented) {
        Rental rental = new Rental();
        rental.setNbOfDays(nbOfDays);
        rental.setRentDate(LocalDate.now().minusDays(howManyDaysAgoRented));
        return rental;
    }
}
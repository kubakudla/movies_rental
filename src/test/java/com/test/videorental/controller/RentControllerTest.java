package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.service.RentService;
import com.test.videorental.validator.RentValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RentControllerTest {

    @InjectMocks
    private RentController rentController;

    @Mock
    private RentService rentService;

    @SuppressWarnings("unused")
    @Mock
    private RentValidator rentValidator;

    @Test
    public void test_rentServiceCalled_validationSuccess() {
        //given
        MovieRentRequestDtoSet movieRentRequestDtoSet = new MovieRentRequestDtoSet();

        //when
        when(rentValidator.validateMovieRequest(CUSTOMER_ID, movieRentRequestDtoSet.getMovieRentRequestDtoSet())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        rentController.rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);

        //then
        verify(rentService, times(1)).rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);
    }

    @Test
    public void test_rentServiceCalled_validationFail() {
        //given
        MovieRentRequestDtoSet movieRentRequestDtoSet = new MovieRentRequestDtoSet();

        //when
        when(rentValidator.validateMovieRequest(CUSTOMER_ID, movieRentRequestDtoSet.getMovieRentRequestDtoSet())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //then
        rentController.rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);
        verify(rentService, times(0)).rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);
    }
}
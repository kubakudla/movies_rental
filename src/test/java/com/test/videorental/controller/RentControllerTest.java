package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.service.RentService;
import com.test.videorental.validator.RentValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    public void test_rentServiceCalled() {
        MovieRentRequestDtoSet movieRentRequestDtoSet = new MovieRentRequestDtoSet();

        rentController.rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);
        verify(rentService, times(1)).rentMovies(CUSTOMER_ID, movieRentRequestDtoSet);
    }
}

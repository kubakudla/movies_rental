package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.service.ReturnService;
import com.test.videorental.validator.ReturnValidator;
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
public class ReturnControllerTest {

    @InjectMocks
    private ReturnController returnController;

    @Mock
    private ReturnService returnService;

    @SuppressWarnings("unused")
    @Mock
    private ReturnValidator returnValidator;

    @Test
    public void test_returnServiceCalled_validationSuccess() {
        //given
        MovieReturnRequestDtoSet movieRentRequestDtoList = new MovieReturnRequestDtoSet();

        //when
        when(returnValidator.validateMovieReturn(CUSTOMER_ID, movieRentRequestDtoList.getMovieReturnRequestDtoSet())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        //then
        returnController.returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
        verify(returnService, times(1)).returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
    }

    @Test
    public void test_returnServiceCalled_validationFailure() {
        //given
        MovieReturnRequestDtoSet movieRentRequestDtoList = new MovieReturnRequestDtoSet();

        //when
        when(returnValidator.validateMovieReturn(CUSTOMER_ID, movieRentRequestDtoList.getMovieReturnRequestDtoSet())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //then
        returnController.returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
        verify(returnService, times(0)).returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
    }
}

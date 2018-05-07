package com.test.videorental.controller;

import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.service.ReturnService;
import com.test.videorental.validator.ReturnValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    public void test_returnServiceCalled() {
        MovieReturnRequestDtoSet movieRentRequestDtoList = new MovieReturnRequestDtoSet();

        returnController.returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
        verify(returnService, times(1)).returnMovies(CUSTOMER_ID, movieRentRequestDtoList);
    }
}

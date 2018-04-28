package com.test.videorental.request;

import com.test.videorental.dto.request.MovieReturnRequestDto;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.test.videorental.validator.ValidationUtil.MOVIE_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieReturnRequestDtoValidator {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_validateReturnRequestDto_success() {

        MovieReturnRequestDto returnRequestDto = new MovieReturnRequestDto();
        returnRequestDto.setMovieId(MOVIE_ID);
        returnRequestDto.setMovieName(MOVIE_NAME);

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void test_validateReturnRequestDto_successMinValues() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieId(1l);
        returnRequestDto.setMovieName("PI");

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void test_validateReturnRequestDto_successMaxValues() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieId(Long.MAX_VALUE);
        returnRequestDto.setMovieName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void test_validateReturnRequestDto_failNoMovieName() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieName(null);

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be empty", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateReturnRequestDto_failEmptyMovieName() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieName("  ");

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be empty", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateReturnRequestDto_failTooShortMovieName() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieName("M");

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("size must be between 2 and 255", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateReturnRequestDto_failTooLongMovieName() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieName("Very long name of a movie which for sure doesn't exists blaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("size must be between 2 and 255", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateReturnRequestDto_failIdZero() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieId(0l);

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("movieId", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateReturnRequestDto_failNegativeId() {
        MovieReturnRequestDto returnRequestDto = initCorrectMovieReturnRequestDto();
        returnRequestDto.setMovieId(-65l);

        Set<ConstraintViolation<MovieReturnRequestDto>> violations = validator.validate(returnRequestDto);
        ConstraintViolation<MovieReturnRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("movieId", constraintViolation.getPropertyPath().toString());
    }

    private MovieReturnRequestDto initCorrectMovieReturnRequestDto() {
        MovieReturnRequestDto movieReturnRequestDto = new MovieReturnRequestDto();
        movieReturnRequestDto.setMovieId(MOVIE_ID);
        movieReturnRequestDto.setMovieName(MOVIE_NAME);
        return movieReturnRequestDto;
    }
}

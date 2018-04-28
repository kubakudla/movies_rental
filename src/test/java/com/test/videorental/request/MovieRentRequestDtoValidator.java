package com.test.videorental.request;

import com.test.videorental.dto.request.MovieRentRequestDto;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.test.videorental.validator.ValidationUtil.MOVIE_ID;
import static com.test.videorental.validator.ValidationUtil.MOVIE_NAME;
import static com.test.videorental.validator.ValidationUtil.NB_OF_DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieRentRequestDtoValidator {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_validateRentRequestDto_success() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void test_validateRentRequestDto_successMinValues() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieId(1l);
        rentRequestDto.setNbOfDays(1);
        rentRequestDto.setMovieName("PI");

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void test_validateRentRequestDto_successMaxValues() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieId(Long.MAX_VALUE);
        rentRequestDto.setNbOfDays(30);
        rentRequestDto.setMovieName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        assertTrue(violations.isEmpty());
    }


    @Test
    public void test_validateRentRequestDto_failNoMovieId() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieId(null);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be null", constraintViolation.getMessage());
        assertEquals("movieId", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failNoNbOfDays() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setNbOfDays(null);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be null", constraintViolation.getMessage());
        assertEquals("nbOfDays", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failNoMovieName() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieName(null);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be empty", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failEmptyMovieName() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieName("  ");

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("may not be empty", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failTooShortMovieName() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieName("M");

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("size must be between 2 and 255", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failTooLongMovieName() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieName("Very long name of a movie which for sure doesn't exists blaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("size must be between 2 and 255", constraintViolation.getMessage());
        assertEquals("movieName", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failIdZero() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieId(0l);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("movieId", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failNegativeId() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setMovieId(-65l);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("movieId", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failNbOfDaysZero() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setNbOfDays(0);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("nbOfDays", constraintViolation.getPropertyPath().toString());
    }

    @Test
    public void test_validateRentRequestDto_failNbOfDaysNegative() {
        MovieRentRequestDto rentRequestDto = initCorrectMovieRentRequestDto();
        rentRequestDto.setNbOfDays(-1);

        Set<ConstraintViolation<MovieRentRequestDto>> violations = validator.validate(rentRequestDto);
        ConstraintViolation<MovieRentRequestDto> constraintViolation = violations.iterator().next();
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", constraintViolation.getMessage());
        assertEquals("nbOfDays", constraintViolation.getPropertyPath().toString());
    }

    private MovieRentRequestDto initCorrectMovieRentRequestDto() {
        MovieRentRequestDto rentRequestDto = new MovieRentRequestDto();
        rentRequestDto.setMovieId(MOVIE_ID);
        rentRequestDto.setNbOfDays(NB_OF_DAYS);
        rentRequestDto.setMovieName(MOVIE_NAME);
        return rentRequestDto;
    }
}

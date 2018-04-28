package com.test.videorental.validator;

import com.test.videorental.dto.request.CommonMovieRequest;
import com.test.videorental.dto.response.WithErrorMessage;
import com.test.videorental.entity.Movie;
import com.test.videorental.repository.CustomerRepository;
import com.test.videorental.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonValidator {

    private final CustomerRepository customerRepository;

    private final MovieRepository movieRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommonValidator(CustomerRepository customerRepository, MovieRepository movieRepository) {
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
    }

    protected boolean customerValid(long customerId, WithErrorMessage responseDto) {
        if (!customerExists(customerId)) {
            responseDto.setErrorMessage("Customer not found with id: " + customerId);
            logger.debug("Customer not found with id: " + customerId);
            return false;
        }
        return true;
    }

    protected boolean moviesDataValid(Set<? extends CommonMovieRequest> movieRequestDtos, WithErrorMessage responseDto) {
        Set<? extends CommonMovieRequest> missingMovies = findMissingMovies(movieRequestDtos);
        if (!missingMovies.isEmpty()) {
            responseDto.setErrorMessage("Movies not found: " + missingMovies);
            logger.debug("Movies not found: " + missingMovies);
            return false;
        }
        return true;
    }

    private boolean customerExists(long customerId) {
        return customerRepository.exists(customerId);
    }

    private Set<? extends CommonMovieRequest> findMissingMovies(Set<? extends CommonMovieRequest> movieRentRequestDtos) {
        Set<Movie> movieList = movieRentRequestDtos.stream().map(m -> movieRepository.findByNameAndId(m.getMovieName().trim(), m.getMovieId())).filter(Objects::nonNull).collect(Collectors.toSet());
        if (!movieList.isEmpty()) {
            Set<Long> ids = movieList.stream().filter(Objects::nonNull).map(Movie::getId).collect(Collectors.toSet());
            return movieRentRequestDtos.stream().filter(m -> !ids.contains(m.getMovieId())).collect(Collectors.toSet());
        }
        return movieRentRequestDtos;
    }
}

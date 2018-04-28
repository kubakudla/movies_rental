package com.test.videorental.validator;

import com.test.videorental.dto.request.MovieRentRequestDto;
import com.test.videorental.dto.request.MovieRentRequestDtoSet;
import com.test.videorental.dto.request.MovieReturnRequestDto;
import com.test.videorental.dto.request.MovieReturnRequestDtoSet;
import com.test.videorental.dto.response.MovieRentWithPriceDto;
import com.test.videorental.dto.response.MovieReturnWithSurchargesDto;
import com.test.videorental.entity.Movie;
import com.test.videorental.entity.Rental;
import com.test.videorental.enums.VideoType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class ValidationUtil {

    public static final Long CUSTOMER_ID = 12l;

    public static final Long MOVIE_ID = 1l;

    public static final String MOVIE_NAME = "Spiderman";

    public static final Integer NB_OF_DAYS = 5;

    public static MovieRentRequestDtoSet initMovieRentRequest() {
        return initMovieRentRequest(1);
    }

    public static MovieRentRequestDtoSet initMovieRentRequest(int nbOfMovies) {
        MovieRentRequestDtoSet movieRentRequestDtoSet = new MovieRentRequestDtoSet();
        Set<MovieRentRequestDto> movieRentRequestDtos = initMovieRentRequestDtoList(nbOfMovies);
        movieRentRequestDtoSet.setMovieRentRequestDtoSet(movieRentRequestDtos);
        return movieRentRequestDtoSet;
    }

    public static MovieReturnRequestDtoSet initMovieReturnRequest() {
        return initMovieReturnRequest(1);
    }

    public static MovieReturnRequestDtoSet initMovieReturnRequest(int nbOfMovies) {
        MovieReturnRequestDtoSet movieReturnRequestDtoSet = new MovieReturnRequestDtoSet();
        Set<MovieReturnRequestDto> movieRentRequestDtos = initMovieReturnRequestDtoList(nbOfMovies);
        movieReturnRequestDtoSet.setMovieReturnRequestDtoSet(movieRentRequestDtos);
        return movieReturnRequestDtoSet;
    }

    public static MovieRentWithPriceDto initMovieRentWithPriceDto(VideoType videoType) {
        return initMovieRentWithPriceDto(videoType, 1);
    }

    public static MovieRentWithPriceDto initMovieRentWithPriceDto(VideoType videoType, int nbOfMovie) {
        //indexing from 0
        nbOfMovie = nbOfMovie - 1;
        MovieRentWithPriceDto movieRentWithPriceDto = new MovieRentWithPriceDto();
        movieRentWithPriceDto.setMovieId(MOVIE_ID + ((nbOfMovie > 0) ? nbOfMovie : 0l));
        movieRentWithPriceDto.setNbOfDays(NB_OF_DAYS);
        movieRentWithPriceDto.setVideoType(videoType);
        movieRentWithPriceDto.setMovieName(MOVIE_NAME + ((nbOfMovie > 0) ? nbOfMovie : ""));
        return movieRentWithPriceDto;
    }

    public static MovieReturnWithSurchargesDto initMovieReturnWithSurcharges(VideoType videoType) {
        return initMovieReturnWithSurcharges(videoType, 1);
    }

    public static MovieReturnWithSurchargesDto initMovieReturnWithSurcharges(VideoType videoType, int nbOfMovie) {
        //indexing from 0
        nbOfMovie = nbOfMovie - 1;
        MovieReturnWithSurchargesDto movieReturnWithSurchargesDto = new MovieReturnWithSurchargesDto();
        movieReturnWithSurchargesDto.setMovieId(MOVIE_ID + ((nbOfMovie > 0) ? nbOfMovie : 0l));
        movieReturnWithSurchargesDto.setVideoType(videoType);
        movieReturnWithSurchargesDto.setMovieName(MOVIE_NAME + ((nbOfMovie > 0) ? nbOfMovie : ""));
        return movieReturnWithSurchargesDto;
    }

    public static Set<MovieRentRequestDto> initMovieRentRequestDtoList() {
        return initMovieRentRequestDtoList(1);
    }

    public static Set<MovieRentRequestDto> initMovieRentRequestDtoList(int nbOfMovies) {
        Set<MovieRentRequestDto> movieRentRequestDtoList = new HashSet<>();
        IntStream.range(0, nbOfMovies).forEach(nbOfMovie -> movieRentRequestDtoList.add(initMovieRentRequestDto(nbOfMovie)));
        return movieRentRequestDtoList;
    }

    private static MovieRentRequestDto initMovieRentRequestDto(int nbOfMovie) {
        MovieRentRequestDto movieRentRequestDto = new MovieRentRequestDto();
        //generate different values
        movieRentRequestDto.setMovieId(MOVIE_ID + ((nbOfMovie > 0) ? nbOfMovie : 0l));
        movieRentRequestDto.setMovieName(MOVIE_NAME + ((nbOfMovie > 0) ? nbOfMovie : ""));
        movieRentRequestDto.setNbOfDays(NB_OF_DAYS);
        return movieRentRequestDto;
    }

    public static Set<MovieReturnRequestDto> initMovieReturnRequestDtoList() {
        return initMovieReturnRequestDtoList(1);
    }

    public static Set<MovieReturnRequestDto> initMovieReturnRequestDtoList(int nbOfMovies) {
        Set<MovieReturnRequestDto> movieReturnRequestDtoList = new HashSet<>();
        IntStream.range(0, nbOfMovies).forEach(nbOfMovie -> movieReturnRequestDtoList.add(initMovieReturnRequestDto(nbOfMovie)));
        return movieReturnRequestDtoList;
    }

    private static MovieReturnRequestDto initMovieReturnRequestDto(int nbOfMovie) {
        MovieReturnRequestDto movieReturnRequestDto = new MovieReturnRequestDto();
        movieReturnRequestDto.setMovieId(MOVIE_ID + ((nbOfMovie > 0) ? nbOfMovie : 0l));
        movieReturnRequestDto.setMovieName(MOVIE_NAME + ((nbOfMovie > 0) ? nbOfMovie : ""));
        return movieReturnRequestDto;
    }

    public static Rental initRental() {
        Rental rental = new Rental();
        Movie movie = initMovie();
        rental.setMovie(movie);
        return rental;
    }

    public static Movie initMovie() {
        Movie movie = new Movie();
        movie.setId(MOVIE_ID);
        movie.setName(MOVIE_NAME);
        return movie;
    }
}
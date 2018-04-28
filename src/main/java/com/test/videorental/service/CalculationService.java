package com.test.videorental.service;

import com.test.videorental.entity.Movie;
import com.test.videorental.entity.Rental;
import com.test.videorental.repository.MovieRepository;
import com.test.videorental.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CalculationService {

    @Value("${price.premium}")
    private String PRICE_PREMIUM;

    @Value("${price.basic}")
    private String PRICE_BASIC;

    @Value("${priceLimit.regular}")
    private String REGULAR_TYPE_PRICE_LIMIT;

    @Value("${priceLimit.old}")
    private String OLD_TYPE_PRICE_LIMIT;

    private final MovieRepository movieRepository;

    private final RentalRepository rentalRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CalculationService(MovieRepository movieRepository, RentalRepository rentalRepository) {
        this.movieRepository = movieRepository;
        this.rentalRepository = rentalRepository;
    }

    public BigDecimal calculatePrice(Long movieId, Integer days) {
        Movie movie = movieRepository.findOne(movieId);
        switch (movie.getVideoType()) {
            case OLD:
                return getIrregularPrice(OLD_TYPE_PRICE_LIMIT, days);
            case REGULAR:
                return getIrregularPrice(REGULAR_TYPE_PRICE_LIMIT, days);
            case NEW_RELEASE:
                return new BigDecimal(PRICE_PREMIUM).multiply(new BigDecimal(days));
            default:
                logger.error("Unsupported movie type: " + movie.getVideoType());
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculateSurcharges(Long customerId, Long movieId) {
        Rental rental = rentalRepository.findActiveRental(customerId, movieId);
        if (isLateReturn(rental)) {
            Movie movie = movieRepository.findOne(movieId);
            long daysLate = calculateDaysLate(rental);
            switch (movie.getVideoType()) {
                case OLD:
                case REGULAR:
                    return new BigDecimal(PRICE_BASIC).multiply(new BigDecimal(daysLate));
                case NEW_RELEASE:
                    return new BigDecimal(PRICE_PREMIUM).multiply(new BigDecimal(daysLate));
                default:
                    logger.error("Unsupported movie type: " + movie.getVideoType());
                    return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public int calculateDaysLate(Long customerId, Long movieId) {
        Rental rental = rentalRepository.findActiveRental(customerId, movieId);
        if (isLateReturn(rental)) {
            return calculateDaysLate(rental);
        }
        return 0;
    }

    private boolean isLateReturn(Rental rental) {
        return rental.getRentDate().plusDays(rental.getNbOfDays()).isBefore(LocalDate.now());
    }

    private Integer calculateDaysLate(Rental rental) {
        return Math.toIntExact(DAYS.between(rental.getRentDate().plusDays(rental.getNbOfDays()), LocalDate.now()));
    }

    private BigDecimal getIrregularPrice(String priceLimit, Integer days) {
        if (days <= Integer.valueOf(priceLimit)) {
            return new BigDecimal(PRICE_BASIC);
        } else {
            return new BigDecimal(PRICE_BASIC).add(new BigDecimal(PRICE_BASIC).multiply(new BigDecimal(days).subtract(new BigDecimal(priceLimit))));
        }
    }
}

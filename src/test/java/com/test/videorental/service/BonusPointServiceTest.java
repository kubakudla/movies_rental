package com.test.videorental.service;

import com.test.videorental.entity.BonusPoint;
import com.test.videorental.entity.Movie;
import com.test.videorental.entity.Rental;
import com.test.videorental.enums.VideoType;
import com.test.videorental.repository.BonusPointRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;

import static com.test.videorental.validator.ValidationUtil.CUSTOMER_ID;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BonusPointServiceTest {

    private BonusPoint bonusPoint;

    private static final Integer START_POINTS = 10;

    @InjectMocks
    private BonusPointService bonusPointService;

    @Mock
    private BonusPointRepository bonusPointRepository;

    @Before
    public void init() {

        ReflectionTestUtils.setField(bonusPointService, "BONUS_POINT_NEW_RELEASE", "2");
        ReflectionTestUtils.setField(bonusPointService, "BONUS_POINT_OTHER", "1");

        bonusPoint = new BonusPoint();
        bonusPoint.setPoints(START_POINTS);
    }

    @Test
    public void test_addBonusPoints_bonusPointSavedAndPointsCalculated() {
        //given
        Rental rental_new = initRental(VideoType.NEW_RELEASE);
        Rental rental_old = initRental(VideoType.OLD);
        Rental rental_regular = initRental(VideoType.REGULAR);

        //when
        when(bonusPointRepository.findByCustomer_id(CUSTOMER_ID)).thenReturn(bonusPoint);
        bonusPointService.addBonusPoints(CUSTOMER_ID, new HashSet<>(Arrays.asList(rental_new, rental_old, rental_regular)));

        //then
        verify(bonusPointRepository, times(1)).save(any(BonusPoint.class));
        assertTrue(START_POINTS + Integer.valueOf(bonusPointService.BONUS_POINT_NEW_RELEASE)
            + 2 * Integer.valueOf(bonusPointService.BONUS_POINT_OTHER) == bonusPoint.getPoints());
    }

    @Test
    public void test_calculatePoints_newReleaseCalculatedCorrectly() {
        //when
        bonusPointService.calculatePoints(bonusPoint, VideoType.NEW_RELEASE);

        //then
        assertTrue(START_POINTS + Integer.valueOf(bonusPointService.BONUS_POINT_NEW_RELEASE) == bonusPoint.getPoints());
    }

    @Test
    public void test_calculatePoints_newOldCalculatedCorrectly() {
        //when
        bonusPointService.calculatePoints(bonusPoint, VideoType.OLD);

        //then
        assertTrue(START_POINTS + Integer.valueOf(bonusPointService.BONUS_POINT_OTHER) == bonusPoint.getPoints());
    }

    @Test
    public void test_calculatePoints_regularOldCalculatedCorrectly() {
        //when
        bonusPointService.calculatePoints(bonusPoint, VideoType.REGULAR);

        //then
        assertTrue(START_POINTS + Integer.valueOf(bonusPointService.BONUS_POINT_OTHER) == bonusPoint.getPoints());
    }

    private Rental initRental(VideoType videoType) {
        Rental rental = new Rental();
        Movie movie = new Movie();
        movie.setVideoType(videoType);
        rental.setMovie(movie);
        return rental;
    }
}

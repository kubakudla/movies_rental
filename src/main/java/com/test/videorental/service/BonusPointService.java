package com.test.videorental.service;

import com.test.videorental.entity.BonusPoint;
import com.test.videorental.entity.Rental;
import com.test.videorental.enums.VideoType;
import com.test.videorental.repository.BonusPointRepository;
import com.test.videorental.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BonusPointService {

    private final BonusPointRepository bonusPointRepository;

    private final CustomerRepository customerRepository;

    @Value("${bonusPoint.newRelease}")
    String BONUS_POINT_NEW_RELEASE;

    @Value("${bonusPoint.other}")
    String BONUS_POINT_OTHER;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BonusPointService(BonusPointRepository bonusPointRepository, CustomerRepository customerRepository) {
        this.bonusPointRepository = bonusPointRepository;
        this.customerRepository = customerRepository;
    }

    public void addBonusPoints(long customerId, Set<Rental> rentalList) {
        final BonusPoint bonusPoint = initBonusPoint(customerId);
        rentalList.stream().forEach(r -> calculatePoints(bonusPoint, r.getMovie().getVideoType()));
        bonusPointRepository.save(bonusPoint);
    }

    private BonusPoint initBonusPoint(long customerId) {
        BonusPoint bonusPoint = bonusPointRepository.findByCustomer_id(customerId);
        if (bonusPoint == null) {
            bonusPoint = new BonusPoint();
            bonusPoint.setPoints(0);
            bonusPoint.setCustomer(customerRepository.findOne(customerId));
        }
        return bonusPoint;
    }

    void calculatePoints(BonusPoint bonusPoint, VideoType videoType) {
        switch (videoType) {
            case NEW_RELEASE:
                bonusPoint.setPoints(bonusPoint.getPoints() + Integer.valueOf(BONUS_POINT_NEW_RELEASE));
                break;
            case REGULAR:
            case OLD:
                bonusPoint.setPoints(bonusPoint.getPoints() + Integer.valueOf(BONUS_POINT_OTHER));
                break;
            default:
                logger.error("Unsupported movie type: " + videoType);
        }
    }
}

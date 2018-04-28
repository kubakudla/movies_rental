package com.test.videorental.repository;

import com.test.videorental.entity.BonusPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusPointRepository extends JpaRepository<BonusPoint, Long> {

    BonusPoint findByCustomer_id(Long customerId);
}

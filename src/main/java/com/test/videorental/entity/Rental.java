package com.test.videorental.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "rent_date")
    private LocalDate rentDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @NotNull
    @Column(name = "nb_of_days")
    private Integer nbOfDays;

    @Column(name = "nb_of_days_late")
    private Integer nbOfDaysLate;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "surcharges")
    private BigDecimal surcharges;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}

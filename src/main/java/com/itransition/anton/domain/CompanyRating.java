package com.itransition.anton.domain;

import javax.persistence.*;

/**
 * Created by qanto on 06.09.2019.
 */

@Entity
public class CompanyRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private Long userId;

    private Integer rating;

    public CompanyRating() {}

    public CompanyRating(Company company, Long userId, Integer rating) {
        this.company = company;
        this.userId = userId;
        this.rating = rating;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

package com.itransition.anton.domain;

import javax.persistence.*;

/**
 * Created by qanto on 18.09.2019.
 */
@Entity
public class CompanyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public CompanyImage(){}

    public CompanyImage(String imageId, Company company) {
        this.imageId = imageId;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

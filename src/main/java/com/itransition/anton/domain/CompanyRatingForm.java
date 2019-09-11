package com.itransition.anton.domain;

import org.springframework.stereotype.Component;

/**
 * Created by qanto on 11.09.2019.
 */
@Component
public class CompanyRatingForm {

    private Long companyId;
    private Integer vote;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "CompanyRatingForm{" +
                "companyId=" + companyId +
                ", vote=" + vote +
                '}';
    }
}
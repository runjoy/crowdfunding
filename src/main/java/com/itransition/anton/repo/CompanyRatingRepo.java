package com.itransition.anton.repo;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.CompanyRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by qanto on 06.09.2019.
 */
public interface CompanyRatingRepo extends CrudRepository<CompanyRating, Long> {
    Optional<CompanyRating> findByUserIdAndCompany(Long UserId, Company company);
}

package com.itransition.anton.repo;

import com.itransition.anton.domain.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by qanto on 05.09.2019.
 */
public interface CompanyRepo extends CrudRepository<Company, Long> {
    Optional<Company> findById(Long id);
    List<Company> findAllByOrderByRatingDesc();
}

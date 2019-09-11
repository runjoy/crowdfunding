package com.itransition.anton.repo;

import com.itransition.anton.domain.Bonus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by qanto on 08.09.2019.
 */
public interface BonusRepo extends CrudRepository<Bonus, Long> {
    Optional<Bonus> findById(Long id);
}
package com.itransition.anton.repo;

import com.itransition.anton.domain.News;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by qanto on 08.09.2019.
 */
public interface NewsRepo extends CrudRepository<News, Long> {
    Optional<News> findById(Long id);
}

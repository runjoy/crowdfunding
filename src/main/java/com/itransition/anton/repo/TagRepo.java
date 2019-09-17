package com.itransition.anton.repo;

import com.itransition.anton.domain.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by qanto on 16.09.2019.
 */
public interface TagRepo extends CrudRepository<Tag, Long> {
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    List<Tag> findByNameStartingWith(String name);
}
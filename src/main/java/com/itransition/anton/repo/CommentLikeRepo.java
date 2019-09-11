package com.itransition.anton.repo;

import com.itransition.anton.domain.CommentLike;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by qanto on 09.09.2019.
 */

public interface CommentLikeRepo extends CrudRepository<CommentLike, Long> {
    Optional<CommentLike> findById(Long id);
}
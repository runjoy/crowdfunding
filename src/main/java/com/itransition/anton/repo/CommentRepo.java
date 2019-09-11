package com.itransition.anton.repo;

import com.itransition.anton.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by qanto on 09.09.2019.
 */
public interface CommentRepo extends CrudRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
}
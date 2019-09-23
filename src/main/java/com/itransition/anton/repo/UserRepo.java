package com.itransition.anton.repo;

import com.itransition.anton.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qanto on 11.08.2019.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByActivationCode(String code);
    User findByFacebookId(String facebookId);
    User findByGoogleId(String googleId);
}

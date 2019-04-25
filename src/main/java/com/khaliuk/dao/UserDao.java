package com.khaliuk.dao;

import com.khaliuk.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll();
    Optional<User> findByUsername(String username);
}

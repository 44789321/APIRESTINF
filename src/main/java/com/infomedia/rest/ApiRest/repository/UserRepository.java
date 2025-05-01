package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll();
    Optional<User> findByUsername(String username);
    User findFirstByOrderByUserIdAsc();
    Optional<User> findByFilterId(Long filterId);
    Optional<User> findByUserId(Long userId);
}

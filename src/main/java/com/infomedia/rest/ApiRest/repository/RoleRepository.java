package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();
    Optional<Role> findById(Long id);
}
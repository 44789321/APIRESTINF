package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> ids);
}
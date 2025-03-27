package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    List<ProjectRole> findByUserFilter(Long userFilter);
}

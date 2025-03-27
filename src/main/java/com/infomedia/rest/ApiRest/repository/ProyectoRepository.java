package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, String> {
    List<Proyecto> findByProjectIdIn(List<String> projectIds);
    Optional<Proyecto> findByProjectId(String projectId);
}


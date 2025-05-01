package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {
    Optional<People> findByPeopleId(Long peopleId);
}





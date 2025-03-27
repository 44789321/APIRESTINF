package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.People;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<People, Long> {
    Optional<People> findByPeopleId(Long peopleId);
}





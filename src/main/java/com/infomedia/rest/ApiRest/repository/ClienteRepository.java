package com.infomedia.rest.ApiRest.repository;

import com.infomedia.rest.ApiRest.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Clientes, Long> {
    Optional<Clientes> findByClienteId(Long clienteId);
}

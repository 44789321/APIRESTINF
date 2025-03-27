package com.infomedia.rest.ApiRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INF_CLIENTES")
public class Clientes {
    @Id
    @Column(name = "ICL_ID")
    private Long clienteId;
    @Column(name = "ICL_NOMBRE")
    private String clienteNombre;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
}

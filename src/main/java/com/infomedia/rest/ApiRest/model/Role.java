package com.infomedia.rest.ApiRest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "INF_ROLES")
public class Role {

    @Id
    @Column(name = "INR_ID")
    private Long id;

    @Column(name = "INR_ROL")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
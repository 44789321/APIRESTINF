package com.infomedia.rest.ApiRest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "INF_USUARIOS")
public class User {

    @Id
    @Column(name = "ISR_USUARIO")
    private String username;

    @JsonIgnore
    @Column(name = "ISR_CONTRA")
    private String password;

    @Column(name = "ISR_INR_ID")
    private Long roleId;

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
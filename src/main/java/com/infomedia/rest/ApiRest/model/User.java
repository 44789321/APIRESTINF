package com.infomedia.rest.ApiRest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "INF_USUARIOS")
public class User {
//---------------------------------Information Requested------------------------------------------
    @Id

    @JsonIgnore
    @Column(name = "ISR_USUARIO")
    private String username;

    @JsonIgnore
    @Column(name = "ISR_ICN_ID")
    private Long filterId;

    @JsonIgnore
    @Column(name = "ISR_CONTRA")
    private String password;

    @Column(name="ISR_ID")
    private Long userId;

    @Column(name = "ISR_INR_ID")
    private Long roleId;

    @Transient
    private Long userFilterId;

    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserFilterId() {
        return userFilterId;
    }

    public void setUserFilterId(Long userFilterId) {
        this.userFilterId = userFilterId;
    }
}
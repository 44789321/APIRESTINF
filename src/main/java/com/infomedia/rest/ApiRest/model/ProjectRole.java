package com.infomedia.rest.ApiRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INF_PRY_CLTOR")
public class ProjectRole {
    @Id
    @Column(name = "IPS_INP_ID_PROYECTO")
    private String projectName;

    @Column(name = "IPS_ICN_ID")
    private Long userFilter;

    @Column(name = "IPS_INR_ID")
    private Long roleName;

    //Getters and Setters

    public Long getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(Long userFilter) {
        this.userFilter = userFilter;
    }

    public Long getRoleName() {
        return roleName;
    }

    public void setRoleName(Long roleName) {
        this.roleName = roleName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}

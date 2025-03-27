package com.infomedia.rest.ApiRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INF_PROYECTOS")
public class Proyecto {
    @Id
    @Column (name = "INP_NOMBRE")
    private String projectName;
    @Column (name = "INP_ID_PROYECTO")
    private String projectId;
    @Column(name = "INP_ICL_ID")
    private Long clientId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}

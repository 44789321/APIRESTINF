package com.infomedia.rest.ApiRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INF_PERSONAS")
public class People {
    @Id
    @Column(name = "ICN_NOMBRE")
    private String userName;

    @Column(name = "ICN_ID")
    private Long peopleId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Long peopleId) {
        this.peopleId = peopleId;
    }
}

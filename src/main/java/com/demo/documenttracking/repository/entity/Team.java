package com.demo.documenttracking.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode
public class Team implements Serializable {

    @Id
    private String name;

    @OneToMany(mappedBy = "team")
    private List<TeamUserRegistration> registrations;
}

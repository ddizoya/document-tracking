package com.demo.documenttracking.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class User implements Serializable {

    @Id
    private String email;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "user")
    private List<TeamUserRegistration> registrations;

    @OneToMany(mappedBy = "user")
    private List<Document> documents;

}

package com.demo.documenttracking.repository;

import com.demo.documenttracking.repository.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {

}


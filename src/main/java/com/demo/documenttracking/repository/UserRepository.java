package com.demo.documenttracking.repository;

import com.demo.documenttracking.repository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmailAndRegistrationsTeamName(String email, String teamName);

    //TODO migrate to @NamedNativeQuery and orm.xml, because it's so large
    @Query(nativeQuery = true, value = "SELECT u.* FROM USER u\n" +
            "WHERE u.email NOT IN (SELECT d.email FROM DOCUMENT d)\n" +
            "   OR u.email IN (SELECT d.email FROM DOCUMENT d WHERE d.uploaded_date  NOT BETWEEN :from AND :to )")
    Page<User> findInactiveUsers(LocalDateTime from, LocalDateTime to, Pageable pageable);

}

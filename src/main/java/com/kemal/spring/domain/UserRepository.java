package com.kemal.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Keno&Kemo on 30.09.2017..
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = (:email)")
    User findByEmail(@Param("email") String email);




    User findById(Long id);
    User findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllEagerly();
}

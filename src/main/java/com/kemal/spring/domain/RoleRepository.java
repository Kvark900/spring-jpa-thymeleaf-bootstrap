package com.kemal.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

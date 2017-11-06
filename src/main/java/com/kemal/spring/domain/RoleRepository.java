package com.kemal.spring.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);

}

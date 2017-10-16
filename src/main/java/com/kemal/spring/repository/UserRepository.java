package com.kemal.spring.repository;

import com.kemal.spring.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Keno&Kemo on 30.09.2017..
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


}

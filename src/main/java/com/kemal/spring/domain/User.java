package com.kemal.spring.domain;

import lombok.Data;

import javax.persistence.*;


/**
 * Created by Keno&Kemo on 30.09.2017..
 */

@Entity
@Data
public  class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String surname;

    private String username;

    private String email;

    @Column(length = 60)
    private String password;

    private boolean enabled;

}

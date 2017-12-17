package com.kemal.spring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kemal.spring.customAnnotations.ValidRoleName;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ValidRoleName
    @Column(unique = true)
    private String name;

    @JsonBackReference
    @ManyToMany (mappedBy = "roles")
    private List<User> users;

    public Role() {
    }

    public Role(final String name) {
        super();
        this.name = name;
    }

    public Role(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

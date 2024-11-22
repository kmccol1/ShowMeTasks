//***************************************************************************************
//
//     Filename: Role.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This is the Role entity class that
//                  will get assigned automatically on registration.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

//***************************************************************************************

@Entity
@Table(name = "roles")
public class Role
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;  // Example values: "ROLE_USER", "ROLE_ADMIN"

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // Constructors
    public Role() {}

    public Role(String name)
    {
        this.name = name;
    }

    // Getters and setters
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

//***************************************************************************************

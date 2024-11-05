package com.kmccol1.to_do_app.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    // Default constructor
    public User()
    {

    }

    //Constructor with username, email, pw in that order (see UserService.java)
    public User(String username, String email, String password)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Getters and setters...
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getId()
    {
        return id;
    }
}
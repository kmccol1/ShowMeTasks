package com.kmccol1.to_do_app.payload;

public class JwtResponse
{
    private String token;
    private Integer id;
    private String username;
    private String email;

    // Updated constructor to accept token, id, username, and email
    public JwtResponse(String token, Integer id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getToken()
    {
        return token;
    }

    public Integer getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }
}

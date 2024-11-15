package com.kmccol1.to_do_app.payload;

public class TaskListResponse
{
    private Integer id;
    private String name;

    //Default constructor...
    public TaskListResponse()
    {

    }

    // Constructor with parameters...
    public TaskListResponse(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
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
}

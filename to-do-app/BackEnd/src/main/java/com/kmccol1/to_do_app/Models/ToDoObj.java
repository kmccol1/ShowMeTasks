package com.kmccol1.to_do_app.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ToDoObj
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private Boolean completed;

    private LocalDateTime createdAt;

    // Foreign key to associate with a TaskList
    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    // Default constructor
    public ToDoObj()
    {

    }

    public ToDoObj(String description, Boolean completed, LocalDateTime createdAt, TaskList taskList)
    {
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
        this.taskList = taskList;
    }

    //Getters and setters...
    public Integer getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Boolean getCompleted()
    {
        return completed;
    }

    public void setCompleted(Boolean completed)
    {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public TaskList getTaskList()
    {
        return taskList;
    }

    public void setTaskList(TaskList taskList)
    {
        this.taskList = taskList;
    }
}
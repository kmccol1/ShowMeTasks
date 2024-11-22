//***************************************************************************************
//
//     Filename: TaskList.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file contains the TaskList
//                  entity, which contains a list of ToDoObjs
//
//***************************************************************************************

package com.kmccol1.to_do_app.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

//***************************************************************************************

@Entity
@Table(name = "task_lists")
public class TaskList
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // Associate each TaskList with a User
    @JsonBackReference(value = "user-taskLists")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Define a relationship with Task
    @JsonManagedReference(value = "taskList-tasks")
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL)
    private List<ToDoObj> tasks = new ArrayList<>();

    // Default constructor
    public TaskList()
    {

    }

    public TaskList(String name, User user)
    {
        this.name = name;
        this.user = user;
    }

    //Setters and getters...
    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public List<ToDoObj> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<ToDoObj> tasks)
    {
        this.tasks = tasks;
    }

    public void addTask(ToDoObj task)
    {
        tasks.add(task);
        task.setTaskList(this);
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}

//***************************************************************************************
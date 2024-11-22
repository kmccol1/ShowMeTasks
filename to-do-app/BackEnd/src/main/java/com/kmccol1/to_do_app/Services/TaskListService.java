//***************************************************************************************
//
//     Filename: TaskListService.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file provides abstracted task list methods.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Data.TaskListRepository;
import com.kmccol1.to_do_app.Exceptions.TaskListNotFoundException;
import com.kmccol1.to_do_app.Exceptions.UserNotFoundException;
import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

@Service
public class TaskListService
{
    @Autowired
    private TaskListRepository taskListRepository;

    /**
     * Creates a new task list.
     */
    public TaskList createTaskList(TaskList taskList)
    {
        // Ensure the user associated with the task list exists.
        if (taskList.getUser() == null)
        {
            throw new UserNotFoundException("User for this task list is missing.");
        }

        return taskListRepository.save(taskList);
    }

    /**
     * Fetch a task list by its ID.
     */
    public TaskList getTaskListById(Integer taskListId)
    {
        Optional<TaskList> taskList = taskListRepository.findById(taskListId);

        // Custom exception for task list not found.
        if (taskList.isEmpty())
        {
            throw new TaskListNotFoundException("Task list with ID " + taskListId + " not found.");
        }

        return taskList.get();
    }

    /**
     * Fetch all task lists for a user.
     */
    public List<TaskList> getTaskListsByUser(User user)
    {
        // Ensure the user exists
        if (user == null || user.getId() == null) // Add user ID check if needed
        {
            throw new UserNotFoundException("User not found.");
        }

        List<TaskList> taskLists = taskListRepository.findByUser(user);

        // If no task lists are found, return an empty list instead of throwing an exception
        if (taskLists.isEmpty())
        {
            return taskLists; // Return empty list
        }

        return taskLists;
    }
}

//***************************************************************************************
//***************************************************************************************
//
//     Filename: ToDoService.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file provides abstracted task-level functionality.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Data.TaskListRepository;
import com.kmccol1.to_do_app.Data.ToDoRepository;
import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

//***************************************************************************************

@Service
public class ToDoService
{
    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    /**
     * Creates a new ToDo task for a user within a TaskList.
     */
    public ToDoObj createToDoForTaskList(ToDoObj toDoObj, TaskList taskList)
    {
        toDoObj.setTaskList(taskList);
        return toDoRepository.save(toDoObj);
    }

    /**
     * Fetch all ToDo tasks for a specific user.
     */
    public List<ToDoObj> getToDosByUser(User user)
    {
        return toDoRepository.findByUser(user);
    }

    /**
     * Fetch all ToDo tasks for a specific TaskList.
     */
    public List<ToDoObj> getToDosByTaskList(TaskList taskList)
    {
        return toDoRepository.findByTaskList(taskList);
    }

    /**
     * Delete a task from a specific TaskList.
     */
    public void deleteTaskById(Integer taskId)
    {
        ToDoObj task = toDoRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        toDoRepository.delete(task);
    }
}

//***************************************************************************************
package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Data.TaskListRepository;
import com.kmccol1.to_do_app.Data.ToDoRepository;
import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToDoService
{
    private final ToDoRepository toDoRepository;
    private final TaskListRepository taskListRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository, TaskListRepository taskListRepository)
    {
        this.toDoRepository = toDoRepository;
        this.taskListRepository = taskListRepository;
    }

    // Method to fetch all tasks
    public List<ToDoObj> getAllToDoObjs()
    {
        return (List<ToDoObj>) toDoRepository.findAll();
    }

    // Method to save a task
    public ToDoObj saveToDoObj(ToDoObj toDoObj)
    {
        return toDoRepository.save(toDoObj);
    }

    // Method to delete a task by ID
    public void deleteToDoObj(Integer id)
    {
        toDoRepository.deleteById(id);
    }

    // Method to create a new task list for a user
    public TaskList createTaskList(User user, String name)
    {
        TaskList taskList = new TaskList(name, user);
        return taskListRepository.save(taskList);
    }

    // Method to retrieve all task lists for a specific user
    public List<TaskList> getTaskListsByUser(User user)
    {
        return taskListRepository.findByUser(user);
    }

    // Method to retrieve all tasks in a specific task list
    public List<ToDoObj> getTasksByTaskList(TaskList taskList)
    {
        return taskList.getTasks();
    }

    // Method to add a new task to a specific task list
    public ToDoObj createTaskInList(String description, TaskList taskList)
    {
        ToDoObj task = new ToDoObj(description, false, LocalDateTime.now(), taskList);
        return toDoRepository.save(task);
    }

    // Method to get a TaskList by ID
    public TaskList getTaskListById(Integer taskListId)
    {
        return taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("Task list not found"));
    }
}
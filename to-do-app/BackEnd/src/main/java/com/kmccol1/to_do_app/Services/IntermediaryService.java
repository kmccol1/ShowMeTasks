package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.payload.RegisterRequest;
import com.kmccol1.to_do_app.payload.ToDoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntermediaryService
{
    @Autowired
    private UserService userService;

    @Autowired
    @Lazy // Lazy load the ToDoService to avoid circular dependency issues...
    private ToDoService toDoService;

    @Autowired
    private TaskListService taskListService;

    /**
     * Registers a new user and creates a default task list with a default task.
     */
    public User registerUserWithDefaultTask(ToDoRequest toDoRequest, String username, String email, String password)
    {
        // Register the user
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        User user = userService.registerUser(registerRequest);

        // Create a default TaskList for the user
        TaskList defaultTaskList = new TaskList("Default Task List", user);
        taskListService.createTaskList(defaultTaskList);

        // Create a default task and assign it to the TaskList
        // Use description and set the other fields accordingly
        ToDoObj newToDo = new ToDoObj(toDoRequest.getDescription(), false, LocalDateTime.now(), defaultTaskList);
        toDoService.createToDoForTaskList(newToDo, defaultTaskList);

        return user;
    }

    /**
     * Retrieves a user's to-do tasks by username.
     */
    public List<ToDoObj> getUserToDos(String username)
    {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return toDoService.getToDosByUser(user);
    }

    /**
     * Create a new TaskList for a user
     */
    public TaskList createTaskList(User user, String name)
    {
        TaskList taskList = new TaskList(name, user);
        return taskListService.createTaskList(taskList);  // Returns TaskList, not TaskListResponse
    }

    /**
     * Get a TaskList by its ID
     */
    public TaskList getTaskListById(Integer taskListId)
    {
        return taskListService.getTaskListById(taskListId); // Assuming this is a method in TaskListService
    }

    /**
     * Get all task lists for a user
     */
    public List<TaskList> getTaskListsByUser(User user)
    {
        return taskListService.getTaskListsByUser(user); // Assuming this is a method in TaskListService
    }

    /**
     * Create a new ToDo task within a specific TaskList
     */
    public ToDoObj createTaskInList(String description, TaskList taskList)
    {
        // Fetch the current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the creation time and assign the task to the TaskList and user
        LocalDateTime creationTime = LocalDateTime.now();
        ToDoObj task = new ToDoObj(description, false, creationTime, taskList);
        task.setUser(user); // Set the user for the ToDoObj

        // Save the task to the database through toDoService
        return toDoService.createToDoForTaskList(task, taskList);
    }

    /**
     * Get all tasks for a specific TaskList
     */
    public List<ToDoObj> getTasksByTaskList(TaskList taskList)
    {
        return toDoService.getToDosByTaskList(taskList); // Assuming this is a method in ToDoService
    }

    // You can add other utility methods as needed.
}

//***************************************************************************************
//
//     Filename: IntermediaryService.java
//     Author: Kyle McColgan
//     Date: 12 December 2024
//     Description: This class acts as a bridge for the two other classes,
//                  the ToDoService.java and TaskListService.java classes.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Exceptions.TaskListNotFoundException;
import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.payload.RegisterRequest;
import com.kmccol1.to_do_app.payload.TaskListDTO;
import com.kmccol1.to_do_app.payload.ToDoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

//***************************************************************************************

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
    public List<TaskListDTO> getTaskListsByUser(User user)
    {
        // Use the updated service method that returns TaskListDTOs
        return taskListService.getTaskListsByUser(user);
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

    public void deleteTaskById(Integer taskId)
    {
        // Use the ToDoService to handle the deletion
        toDoService.deleteTaskById(taskId);
    }

    public TaskList updateTaskList(Integer id, TaskList updatedTaskList, User user)
    {
        // Ensure the task list is not null
        if (updatedTaskList == null)
        {
            throw new IllegalArgumentException("Task list cannot be null.");
        }

        // Check if the name is null or too long
        if ( ((updatedTaskList.getName()) == null) || ((updatedTaskList.getName().length()) > 255))
        {
            throw new IllegalArgumentException("Error: Task list name is too long.");
        }

        // Check if the user is null or invalid
        if (user == null)
        {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getId() == null || user.getId() <= 0)
        {
            throw new IllegalArgumentException("User is invalid.");
        }

        // Find the existing task list
        TaskList existingTaskList = taskListService.getTaskListById(id);

        // Handle case where task list is not found
        if (existingTaskList == null)
        {
            throw new TaskListNotFoundException("Task list with ID " + id + " not found.");
        }

        // Verify ownership first, before checking for name validity
        if (!existingTaskList.getUser().getId().equals(user.getId()))
        {
            throw new RuntimeException("You do not have permission to update this task list.");
        }

        // Check if the task list is soft-deleted
        if (existingTaskList.isDeleted())
        {
            throw new IllegalArgumentException("Task list has been deleted and cannot be updated.");
        }

        // Check for an empty task list name
        if ( (updatedTaskList.getName() == null) || (updatedTaskList.getName().trim().isEmpty()) )
        {
            throw new IllegalArgumentException("Task list name cannot be empty.");
        }

        // Update fields
        if ( ((updatedTaskList.getName()) != null) && (!(updatedTaskList.getName().isEmpty())))
        {
            existingTaskList.setName(updatedTaskList.getName());
        }

        // Save the updated task list
        return taskListService.saveTaskList(existingTaskList);
    }

//    public TaskList updateTaskList(Integer id, TaskList updatedTaskList, User user) {
//        // Ensure the task list is not null
//        if (updatedTaskList == null) {
//            throw new IllegalArgumentException("Task list cannot be null.");
//        }
//
//        // Check if the user is null or invalid
//        if (user == null || user.getId() == null || user.getId() <= 0) {
//            throw new IllegalArgumentException("User is invalid.");
//        }
//
//        // Find the existing task list
//        TaskList existingTaskList = taskListService.getTaskListById(id);
//
//        // Handle case where task list is not found
//        if (existingTaskList == null) {
//            throw new TaskListNotFoundException("Task list with ID " + id + " not found.");
//        }
//
//        // Verify ownership first, before checking for name validity
//        if (!existingTaskList.getUser().getId().equals(user.getId())) {
//            throw new RuntimeException("You do not have permission to update this task list.");
//        }
//
//        // Check if the task list is soft-deleted
//        if (existingTaskList.isDeleted()) {
//            throw new IllegalArgumentException("Task list has been deleted and cannot be updated.");
//        }
//
//        // Check for an empty task list name
//        if (updatedTaskList.getName() == null || updatedTaskList.getName().trim().isEmpty()) {
//            throw new IllegalArgumentException("Task list name cannot be empty.");
//        }
//
//        // Check if task list name is too long
//        if (updatedTaskList.getName().length() > 255) {
//            throw new IllegalArgumentException("Task list name is too long.");
//        }
//
//        // Update fields
//        if (updatedTaskList.getName() != null && !updatedTaskList.getName().isEmpty()) {
//            existingTaskList.setName(updatedTaskList.getName());
//        }
//
//        // Save the updated task list
//        return taskListService.saveTaskList(existingTaskList);
//    }

    public TaskList getDefaultTaskListForCurrentUser()
    {
        // Retrieve the current authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        // Check if the user already has a default task list
        TaskList defaultTaskList = taskListService.findDefaultTaskListForUser(currentUser);

        // If no default task list exists, create one
        if (defaultTaskList == null) {
            defaultTaskList = taskListService.createDefaultTaskListForUser(currentUser);
        }

        return defaultTaskList;
    }

    public TaskList getOrCreateDefaultTaskListForUser(User user)
    {
        // Check if the user already has a default task list
        TaskList defaultTaskList = taskListService.findDefaultTaskListForUser(user);

        if (defaultTaskList == null)
        {
            // Create a new default task list
            defaultTaskList = new TaskList();
            defaultTaskList.setName("Default Task List");
            defaultTaskList.setUser(user);
            defaultTaskList.setDefault(true);

            // Save the new task list to the database
            defaultTaskList = taskListService.saveTaskList(defaultTaskList);
        }

        return defaultTaskList;
    }

}

//***************************************************************************************


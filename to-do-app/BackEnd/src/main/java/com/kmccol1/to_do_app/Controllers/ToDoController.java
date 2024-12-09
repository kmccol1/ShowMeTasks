//***************************************************************************************
//
//   Filename: ToDoController.java
//   Author: Kyle McColgan
//   Date: 08 December 2024
//   Description: This file implements task list creation functionality.
//
//***************************************************************************************

package com.kmccol1.to_do_app.Controllers;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.Services.IntermediaryService;
import com.kmccol1.to_do_app.Services.UserService;
import com.kmccol1.to_do_app.payload.TaskCreateRequest;
import com.kmccol1.to_do_app.payload.TaskListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//***************************************************************************************

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class ToDoController
{
    @Autowired
    private IntermediaryService intermediaryService;  // Use IntermediaryService instead of ToDoService

    @Autowired
    private UserService userService;

    // Create a new task list for a user
    @PostMapping("/list/create")
    public ResponseEntity<TaskListResponse> createTaskList(@RequestBody TaskList taskList)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated user: " + username);

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_USER")))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        taskList.setUser(user);

        // Use intermediary service to create the task list
        TaskList createdTaskList = intermediaryService.createTaskList(user, taskList.getName());

        // Create and return a simplified TaskListResponse
        TaskListResponse response = new TaskListResponse(createdTaskList.getId(), createdTaskList.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Create a new task within a specific task list
    @PostMapping("/create")
    public ResponseEntity<ToDoObj> createTaskInList(@RequestBody TaskCreateRequest taskCreateRequest)
    {
        Integer taskListId = taskCreateRequest.getTaskListId();
        String description = taskCreateRequest.getDescription();

        // Fetch the TaskList, either by ID or by default
        TaskList taskList;
        if (taskListId == null)
        {
            taskList = intermediaryService.getDefaultTaskListForCurrentUser();
        }
        else
        {
            taskList = intermediaryService.getTaskListById(taskListId);
        }

        // Create the new task
        ToDoObj task = intermediaryService.createTaskInList(description, taskList);
        return ResponseEntity.ok(task);
    }

    // Get all task lists for a specific user
    @GetMapping("/list/{username}")
    public ResponseEntity<List<TaskList>> getTaskListsByUser(@PathVariable String username)
    {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<TaskList> taskLists = intermediaryService.getTaskListsByUser(user);

        System.out.println("Returning task lists: " + taskLists); //For debugging purposes...

        if (taskLists.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(taskLists);
        }

        return ResponseEntity.ok(taskLists);
    }

    // Get all tasks within a specific task list
    @GetMapping("/{taskListId}")
    public ResponseEntity<List<ToDoObj>> getTasksByTaskList(@PathVariable Integer taskListId)
    {
        // Use IntermediaryService to get the task list and tasks
        TaskList taskList = intermediaryService.getTaskListById(taskListId);
        List<ToDoObj> tasks = intermediaryService.getTasksByTaskList(taskList);

        if (tasks.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tasks); // Return 204 if no tasks are found
        }

        return ResponseEntity.ok(tasks);
    }

    // Delete a task by its ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId)
    {
        try
        {
            // Use IntermediaryService to delete the task
            intermediaryService.deleteTaskById(taskId);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if task not found
        }
    }

    @PutMapping("/list/update/{id}")
    public ResponseEntity<TaskListResponse> updateTaskList(
            @PathVariable("id") Integer id,
            @RequestBody TaskList updatedTaskList)
    {
        // Get the authenticated username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated user: " + username);

        // Check if the user has the required role
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_USER")))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Find the user by username
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delegate the update operation to the intermediary service
        TaskList updatedList = intermediaryService.updateTaskList(id, updatedTaskList, user);

        // Create and return a simplified response
        TaskListResponse response = new TaskListResponse(updatedList.getId(), updatedList.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasklists/default")
    public ResponseEntity<TaskList> getOrCreateDefaultTaskList()
    {
        // Retrieve the currently authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Use the IntermediaryService to get or create the default task list
        TaskList defaultTaskList = intermediaryService.getOrCreateDefaultTaskListForUser(currentUser);

        return ResponseEntity.ok(defaultTaskList);
    }
}

//***************************************************************************************
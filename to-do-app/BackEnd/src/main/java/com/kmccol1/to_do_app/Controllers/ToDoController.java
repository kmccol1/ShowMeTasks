//***************************************************************************************
//
//   Filename: ToDoController.java
//   Author: Kyle McColgan
//   Date: 19 December 2024
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
import com.kmccol1.to_do_app.payload.TaskListDTO;
import com.kmccol1.to_do_app.payload.TaskListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//***************************************************************************************

/**
 * REST controller for managing to-do tasks.
 * <p>
 * This controller provides endpoints for creating, retrieving, updating,
 * and deleting to-do tasks. It is mapped to the "/api/todos" base path
 * and supports cross-origin requests from the React frontend at
 * "http://localhost:3000".
 * </p>
 * <p>
 * Endpoints include:
 * <ul>
 *   <li>GET /api/todos - Retrieve a list of all tasks</li>
 *   <li>POST /api/todos - Create a new task</li>
 *   <li>PUT /api/todos/{id} - Update an existing task</li>
 *   <li>DELETE /api/todos/{id} - Delete a task</li>
 * </ul>
 * </p>
 *
 * <p>
 * Cross-Origin Resource Sharing (CORS) is configured to allow requests
 * from the frontend running at <code>http://localhost:3000</code>.
 * </p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @RestController} - Marks this class as a RESTful controller.</li>
 *   <li>{@code @RequestMapping("/api/todos")} - Maps the base path for the endpoints.</li>
 *   <li>{@code @CrossOrigin} - Configures CORS to allow requests from the frontend.</li>
 * </ul>
 *
 * @author Kyle McColgan
 * @version 0.1.0
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class ToDoController
{
    @Autowired
    private IntermediaryService intermediaryService;  // Use IntermediaryService instead of ToDoService

    @Autowired
    private UserService userService;

    /**
     * Creates a new task list for a user.
     * <p>
     * This endpoint accepts a {@link TaskList} object in the request body, processes it,
     * and creates a new task list associated with the user. The newly created task list
     * is returned in the response along with an appropriate HTTP status code.
     * </p>
     *
     * <p>Request:</p>
     * <pre>
     * POST /api/todos/list/create
     * Content-Type: application/json
     * {
     *   "name": "My Task List",
     *   "description": "This is a sample task list."
     * }
     * </pre>
     *
     * <p>Response:</p>
     * <pre>
     * HTTP/1.1 201 Created
     * Content-Type: application/json
     * {
     *   "id": 1,
     *   "name": "My Task List",
     *   "description": "This is a sample task list.",
     *   "tasks": []
     * }
     * </pre>
     *
     * @param taskList the {@link TaskList} object containing the details of the new task list
     * @return a {@link ResponseEntity} containing the created task list wrapped in a
     *         {@link TaskListResponse} object and the HTTP status code
     * @throws IllegalArgumentException if the request body is invalid
     * @see TaskList
     * @see TaskListResponse
     */
    @PostMapping("/list/create")
    public ResponseEntity<TaskListResponse> createTaskList(@RequestBody TaskList taskList)
    {
        // Create a new task list for a user
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

    /**
     * Creates a new task within a specific task list.
     * <p>
     * This endpoint accepts a {@link TaskCreateRequest} object in the request body,
     * processes it, and creates a new task associated with the specified task list.
     * The created task is returned in the response along with an appropriate HTTP
     * status code.
     * </p>
     *
     * <p>Request:</p>
     * <pre>
     * POST /api/todos/create
     * Content-Type: application/json
     * {
     *   "taskListId": 1,
     *   "taskName": "Buy Groceries",
     *   "taskDescription": "Get milk, eggs, and bread.",
     *   "dueDate": "2024-12-20T12:00:00"
     * }
     * </pre>
     *
     * <p>Response:</p>
     * <pre>
     * HTTP/1.1 201 Created
     * Content-Type: application/json
     * {
     *   "id": 42,
     *   "taskListId": 1,
     *   "taskName": "Buy Groceries",
     *   "taskDescription": "Get milk, eggs, and bread.",
     *   "dueDate": "2024-12-20T12:00:00",
     *   "status": "PENDING"
     * }
     * </pre>
     *
     * @param taskCreateRequest the {@link TaskCreateRequest} object containing the details of the task to be created
     * @return a {@link ResponseEntity} containing the created task wrapped in a {@link ToDoObj} object and the HTTP status code
     * @throws IllegalArgumentException if the request body is invalid or the task list ID does not exist
     * @see TaskCreateRequest
     * @see ToDoObj
     */
    @PostMapping("/create")
    public ResponseEntity<ToDoObj> createTaskInList(@RequestBody TaskCreateRequest taskCreateRequest)
    {
        // Create a new task within a specific task list

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
    public ResponseEntity<List<TaskListDTO>> getTaskListsByUser(@PathVariable String username)
    {
        // Find user by username or throw an exception if not found
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch task lists as DTOs
        List<TaskListDTO> taskLists = intermediaryService.getTaskListsByUser(user);

        // Debugging: Print the task lists to the console
        System.out.println("Returning task lists: " + taskLists);

        // Return a 204 No Content response if the list is empty
        if (taskLists.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(taskLists);
        }

        // Otherwise, return the task lists with a 200 OK status
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
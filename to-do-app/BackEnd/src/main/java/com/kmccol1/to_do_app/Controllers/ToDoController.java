package com.kmccol1.to_do_app.Controllers;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.Services.IntermediaryService;
import com.kmccol1.to_do_app.Services.UserService;
import com.kmccol1.to_do_app.payload.TaskListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ToDoObj> createTaskInList(@RequestParam Integer taskListId, @RequestParam String description)
    {
        // Use IntermediaryService to get the task list and create a task
        TaskList taskList = intermediaryService.getTaskListById(taskListId);
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
}
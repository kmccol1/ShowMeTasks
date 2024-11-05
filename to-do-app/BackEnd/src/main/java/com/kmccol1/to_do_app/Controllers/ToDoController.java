package com.kmccol1.to_do_app.Controllers;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.Services.ToDoService;
import com.kmccol1.to_do_app.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class ToDoController
{
    @Autowired
    private ToDoService toDoService;

    @Autowired
    private UserService userService;

    // Create a new task list for a user
    @PostMapping("/list/create")
    public ResponseEntity<TaskList> createTaskList(@RequestParam String username, @RequestParam String name)
    {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        TaskList taskList = toDoService.createTaskList(user, name);
        return ResponseEntity.ok(taskList);
    }

    // Create a new task within a specific task list
    @PostMapping("/create")
    public ResponseEntity<ToDoObj> createTaskInList(@RequestParam Integer taskListId, @RequestParam String description)
    {
        TaskList taskList = toDoService.getTaskListById(taskListId);
        ToDoObj task = toDoService.createTaskInList(description, taskList);
        return ResponseEntity.ok(task);
    }

    // Get all task lists for a specific user
    @GetMapping("/list/{username}")
    public ResponseEntity<List<TaskList>> getTaskListsByUser(@PathVariable String username)
    {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<TaskList> taskLists = toDoService.getTaskListsByUser(user);
        return ResponseEntity.ok(taskLists);
    }

    // Get all tasks within a specific task list
    @GetMapping("/{taskListId}")
    public ResponseEntity<List<ToDoObj>> getTasksByTaskList(@PathVariable Integer taskListId)
    {
        TaskList taskList = toDoService.getTaskListById(taskListId);
        List<ToDoObj> tasks = toDoService.getTasksByTaskList(taskList);
        return ResponseEntity.ok(tasks);
    }
}
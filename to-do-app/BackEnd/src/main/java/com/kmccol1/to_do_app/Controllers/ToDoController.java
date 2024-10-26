package com.kmccol1.to_do_app.Controllers;

import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class ToDoController
{
    @Autowired
    private ToDoService toDoService;

    @GetMapping
    public List<ToDoObj> getAllToDoObjs()
    {
        return toDoService.getAllToDoObjs();
    }

    @PostMapping
    public ToDoObj createToDoObj(@RequestBody ToDoObj toDoObj)
    {
        return toDoService.saveToDoObj(toDoObj);
    }

    @DeleteMapping("/{id}")
    public void deleteToDoObj(@PathVariable Integer id)
    {
        toDoService.deleteToDoObj(id);
    }
}
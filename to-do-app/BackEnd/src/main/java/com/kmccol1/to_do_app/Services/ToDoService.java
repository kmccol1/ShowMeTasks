package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Data.ToDoRepository;
import com.kmccol1.to_do_app.Models.ToDoObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService
{
    @Autowired
    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository)
    {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDoObj> getAllToDoObjs()
    {
        return (List<ToDoObj>) toDoRepository.findAll();
    }

    public ToDoObj saveToDoObj(ToDoObj toDoObj)
    {
        return toDoRepository.save(toDoObj);
    }

    public void deleteToDoObj(Integer id)
    {
        toDoRepository.deleteById(id);
    }
}
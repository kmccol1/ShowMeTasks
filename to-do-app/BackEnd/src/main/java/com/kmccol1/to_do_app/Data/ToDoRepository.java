package com.kmccol1.to_do_app.Data;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.ToDoObj;
import com.kmccol1.to_do_app.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends CrudRepository<ToDoObj, Integer>
{
    List<ToDoObj> findByUser(User user);           // Method to find ToDos by User
    List<ToDoObj> findByTaskList(TaskList taskList);  // Method to find ToDos by TaskList
}
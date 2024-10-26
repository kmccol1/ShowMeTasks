package com.kmccol1.to_do_app.Data;

import com.kmccol1.to_do_app.Models.ToDoObj;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends CrudRepository<ToDoObj, Integer>
{

}
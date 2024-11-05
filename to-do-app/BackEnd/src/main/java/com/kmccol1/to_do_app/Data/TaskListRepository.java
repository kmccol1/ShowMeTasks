package com.kmccol1.to_do_app.Data;

import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends CrudRepository<TaskList, Integer>
{
    // Custom query to find task lists by a specific user
    List<TaskList> findByUser(User user);
}

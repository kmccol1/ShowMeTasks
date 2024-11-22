//***************************************************************************************
//
//     Filename: TaskListServiceTests.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file provides a unit test suite for the task list functions.
//
//***************************************************************************************

package com.kmccol1.to_do_app;

import com.kmccol1.to_do_app.Data.TaskListRepository;
import com.kmccol1.to_do_app.Exceptions.TaskListNotFoundException;
import com.kmccol1.to_do_app.Exceptions.UserNotFoundException;
import com.kmccol1.to_do_app.Models.TaskList;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.Services.TaskListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

public class TaskListServiceTests
{

	@Mock
	private TaskListRepository taskListRepository;  // Mocking the repository

	@InjectMocks
	private TaskListService taskListService;  // Injecting the mocks into the service class

	private User testUser;
	private TaskList testTaskList;

	@BeforeEach
	public void setUp()
	{
		MockitoAnnotations.openMocks(this);  // Initialize mocks
		testUser = new User();
		testUser.setId(1);
		testUser.setUsername("testuser");

		testTaskList = new TaskList();
		testTaskList.setId(1);
		testTaskList.setName("Test Task List");
		testTaskList.setUser(testUser);
	}

	@Test
	public void testCreateTaskList()
	{
		// Arrange
		when(taskListRepository.save(any(TaskList.class))).thenReturn(testTaskList);

		// Act
		TaskList result = taskListService.createTaskList(testTaskList);

		// Assert
		assertNotNull(result);
		assertEquals("Test Task List", result.getName());
		verify(taskListRepository, times(1)).save(testTaskList);
	}

	@Test
	public void testGetTaskListById_found()
	{
		// Arrange
		when(taskListRepository.findById(1)).thenReturn(Optional.of(testTaskList));

		// Act
		TaskList result = taskListService.getTaskListById(1);

		// Assert
		assertNotNull(result);
		assertEquals("Test Task List", result.getName());
	}

	@Test
	public void testGetTaskListById_notFound()
	{
		// Arrange
		when(taskListRepository.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		TaskListNotFoundException exception = assertThrows(TaskListNotFoundException.class, () -> {
			taskListService.getTaskListById(1);
		});
		assertEquals("Task list with ID 1 not found.", exception.getMessage());
	}

	@Test
	public void testGetTaskListsByUser_found()
	{
		// Arrange
		List<TaskList> taskLists = new ArrayList<>();
		taskLists.add(testTaskList);
		when(taskListRepository.findByUser(testUser)).thenReturn(taskLists);

		// Act
		List<TaskList> result = taskListService.getTaskListsByUser(testUser);

		// Assert
		assertEquals(1, result.size());
		assertEquals("Test Task List", result.get(0).getName());
	}

	@Test
	public void testGetTaskListsByUser_notFound()
	{
		// Arrange
		List<TaskList> taskLists = new ArrayList<>();
		when(taskListRepository.findByUser(testUser)).thenReturn(taskLists);

		// Act
		List<TaskList> result = taskListService.getTaskListsByUser(testUser);

		// Assert
		assertTrue(result.isEmpty());
	}

	@Test
	public void testGetTaskListsByUser_userNotFound()
	{
		// Arrange
		User nonExistentUser = new User();
		nonExistentUser.setId(null); // Ensure the user has no ID (simulating a non-existent user)

		// Mock the repository to return an empty list, simulating no task lists for this user
		when(taskListRepository.findByUser(nonExistentUser)).thenReturn(new ArrayList<>());

		// Act & Assert
		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			taskListService.getTaskListsByUser(nonExistentUser);  // This should now throw exception if user is not found
		});

		assertEquals("User not found.", exception.getMessage());
	}
}

//***************************************************************************************
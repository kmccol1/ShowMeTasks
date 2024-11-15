package com.kmccol1.to_do_app.Exceptions;

public class TaskListNotFoundException extends RuntimeException
{
    public TaskListNotFoundException(String message)
    {
        super(message);
    }
}

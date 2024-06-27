package com.todoapp.todoapp.service;


import com.todoapp.todoapp.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final List<Todo> todos = new ArrayList<>();
    private static final AtomicLong idCount = new AtomicLong();
    public List<Todo> findAll(){
        return todos;
    }

    public Todo findById(Long id){
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().orElseThrow(()-> new RuntimeException("There is no such To do element"));
    }

    public List<Todo> filter(String text, Todo.Priority priority, Boolean isDone){
        return todos.stream().
                filter(todo -> (text == null || todo.getText().equals(text))).
                filter(todo -> (priority == null || todo.getPriority().equals(priority))).
                filter(todo -> (isDone == null || todo.isDone() == isDone)).
                collect(Collectors.toList());
    }

    public boolean create (Todo todo){
        if(todo.getId()==null){
            todo.setId(idCount.incrementAndGet());
            todo.setCreationDate(LocalDateTime.now());
            return todos.add(todo);
        }else {
            throw new RuntimeException("A new ToDo must have a null value on id");
        }
    }

    public int update(Todo todo){
        if(todos.contains(todo)){
            int index = todos.indexOf(todo);
            todos.set(index,todo);
            return index;
        }else{
            throw new RuntimeException("There is no such To do element");
        }
    }

    public boolean  remove(Long id){
        if(findById(id)!=null){
            return todos.removeIf(todo -> todo.getId().equals(id));
        }else{
            throw new RuntimeException("There is no such To do element");
        }
    }


}

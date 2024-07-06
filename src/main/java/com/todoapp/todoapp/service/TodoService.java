package com.todoapp.todoapp.service;


import com.todoapp.todoapp.model.Todo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final List<Todo> todos = new ArrayList<>();
    private static final AtomicLong idCount = new AtomicLong();
    private int pageSize = 10;
    public List<Todo> findAll(){
        return todos;
    }

    public Todo findById(Long id){
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().orElseThrow(()-> new RuntimeException("There is no such To do element"));
    }

    public Map<String,Object> filter(String text, Todo.Priority priority, Boolean isDone, int page){
        List<Todo> filteredTodos = todos.stream().
                filter(todo -> (text == null || todo.getText().contains(text))).
                filter(todo -> (priority == null || todo.getPriority().equals(priority))).
                filter(todo -> (isDone == null || todo.isDone() == isDone)).collect(Collectors.toList());
        int first = (page-1)*this.pageSize;
        int last = Math.min(first+this.pageSize, filteredTodos.size());

        int totalPages= (int) Math.ceil((double) filteredTodos.size() /pageSize);
        Map<String,Object> result = new HashMap<>();
        result.put("currentPage", page);
        result.put("totalPages",totalPages);
        result.put("todosList", filteredTodos.subList(first,last));
        System.out.println(priority);
        return result;
    }

    public boolean create (@RequestBody Todo todo){
        if(todo.getId()==null){
            todo.setId(idCount.incrementAndGet());
            todo.setCreationDate(LocalDateTime.now());
            return todos.add(todo);
        }else {
            throw new RuntimeException("A new ToDo must have a null value on id");
        }
    }

    public int update(Long id,Todo todo){
        Todo todoCurrent = findById(id);
        if(todoCurrent != null){
            int index = todos.indexOf(todoCurrent);
            todo.setId(id);
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

    public void markAsDone(Long id){
        Todo todo = findById(id);
        todo.setDone(true);
        todo.setDoneDate(LocalDateTime.now());
    }

    public void markAsUndone(Long id){
        Todo todo = findById(id);
        todo.setDone(false);
        todo.setDoneDate(null);
    }

    public Map<String,Double> getMetrics(){
        Map<Todo.Priority,List<Todo>> doneTodosByPriority = todos.stream().filter(Todo::isDone).collect(Collectors.groupingBy(Todo::getPriority));
        Map<String,Double> averageTimes = new HashMap<>();
        averageTimes.put("ALL",0.0);
        averageTimes.put("HIGH",0.0);
        averageTimes.put("MEDIUM",0.0);
        averageTimes.put("LOW",0.0);
        double totalTime = 0;
        int completedTodos = 0;
        for(Map.Entry<Todo.Priority, List<Todo>> entry : doneTodosByPriority.entrySet()){
            Todo.Priority priority = entry.getKey();
            List<Todo> todosByPriority = entry.getValue();
            double timeByPriority = 0;
            for(Todo todo : todosByPriority){
                long minutes = ChronoUnit.MINUTES.between(todo.getCreationDate(),todo.getDoneDate());
                timeByPriority+=minutes;
            }
            double averageTimeByPriority = timeByPriority/todosByPriority.size();
            averageTimes.put(priority.name(),averageTimeByPriority);

            totalTime+=timeByPriority;
            completedTodos+=todosByPriority.size();

        }
        if(completedTodos>0){
            double averageTime = totalTime/completedTodos;
            averageTimes.put("ALL", averageTime);
        }
        return averageTimes;
    }


}

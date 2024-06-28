package com.todoapp.todoapp.controller;


import com.todoapp.todoapp.model.Todo;
import com.todoapp.todoapp.service.TodoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<?> getTodos(@RequestParam String text, @RequestParam Todo.Priority priority, @RequestParam Boolean isDone){
        return ResponseEntity.ok(todoService.filter(text,priority,isDone));
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo){
        try{
            todoService.create(todo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfuly created To do" + todo.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo newTodo){
        try{
            todoService.update(id,newTodo);
            return ResponseEntity.status(HttpStatus.OK).body("Successfuly updated To do" + newTodo.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<?> doneTodo(@PathVariable Long id){
        try{
            todoService.markAsDone(id);
            return ResponseEntity.ok("Successfuly marked as done");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/undone")
    public ResponseEntity<?> undoneTodo(@PathVariable Long id){
        try{
            todoService.markAsUndone(id);
            return ResponseEntity.ok("Successfuly marked as undone");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}

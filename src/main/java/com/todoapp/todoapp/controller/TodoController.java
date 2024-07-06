package com.todoapp.todoapp.controller;


import com.todoapp.todoapp.model.Todo;
import com.todoapp.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="*")
@Controller
@RequestMapping(path = "/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<?> getTodos(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) Todo.Priority priority,
                                      @RequestParam(required = false) Boolean state,
                                      @RequestParam(defaultValue = "1") int page
                                      ){
        return ResponseEntity.ok(todoService.filter(text,priority,state,page));
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@Validated @RequestBody Todo todo){
        try{
            todoService.create(todo);
            System.out.println(todo.getText());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfuly created To do" + todo.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id,@Validated @RequestBody Todo todo){
        try{
            todoService.update(id,todo);

            return ResponseEntity.status(HttpStatus.OK).body("Successfuly updated To do" + todo.getId());
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

    @PutMapping("/{id}/undone")
    public ResponseEntity<?> undoneTodo(@PathVariable Long id){
        try{
            todoService.markAsUndone(id);
            return ResponseEntity.ok("Successfuly marked as undone");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id){
        try{
            todoService.remove(id);
            return ResponseEntity.ok("Successfuly removed");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics(){
        return ResponseEntity.ok(todoService.getMetrics());
    }


}

package com.todoapp.todoapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.todoapp.model.Todo;
import com.todoapp.todoapp.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.IterableConfigurationPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoService todoService;

    private Todo testTodo;

    @BeforeEach
    public void prepare(){
        testTodo = new Todo("Test todo",LocalDateTime.now().plusDays(1), false, null, Todo.Priority.MEDIUM, LocalDateTime.now());
        todoService.findAll().clear();
        TodoService.idCount.set(1);
    }

    @Test
    public void testGetTodo() throws Exception{
        todoService.create(this.testTodo);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todosList[0].text").value("Test todo"));
    }

    @Test
    public void testCreateTodo() throws Exception{
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.testTodo)))
                .andExpect(status().isCreated());

        List<Todo> todoList = todoService.findAll();
        assertThat(todoList).hasSize(1);
        assertThat(todoList.get(0).getText()).isEqualTo("Test todo");

    }

    @Test
    public void testUpdateTodo() throws Exception{
        Todo updatedTodo = new Todo("Test todo",LocalDateTime.now().plusDays(1), false, null, Todo.Priority.MEDIUM, LocalDateTime.now());
        todoService.create(updatedTodo);
        System.out.println("First id " +todoService.findAll().get(0).getId());
        Long id = updatedTodo.getId();
        updatedTodo.setText("Updated todo");

        mockMvc.perform(put("/todos/{id}",id).
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated To do" + updatedTodo.getId()));

        Todo todo = todoService.findById(id);
        assertThat(todo.getText()).isEqualTo("Updated todo");
    }

    @Test
    public void testDoneTodo() throws Exception{
        todoService.create(testTodo);
        Long id = testTodo.getId();

        mockMvc.perform(post("/todos/{id}/done",id))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully marked as done"));

        Todo doneTodo = todoService.findById(id);
        assertThat(doneTodo.isDone()).isTrue();
        assertThat(doneTodo.getDoneDate()).isNotNull();
    }

    @Test
    public void testUndoneTodo() throws Exception{
        todoService.create(testTodo);
        testTodo.setDone(true);
        testTodo.setDoneDate(LocalDateTime.now());
        Long id = testTodo.getId();

        mockMvc.perform(put("/todos/{id}/undone",id))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully marked as undone"));

        Todo undoneTodo = todoService.findById(id);
        assertThat(undoneTodo.isDone()).isFalse();
        assertThat(undoneTodo.getDoneDate()).isNull();
    }

    @Test
    public void testDeleteTodo() throws Exception{
        todoService.create(testTodo);
        Long id = testTodo.getId();;

        mockMvc.perform(post("/todos/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully removed"));

        assertThat(todoService.findAll()).isEmpty();
    }



}

package com.todoapp.todoapp.model;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Todo {

    private static final AtomicLong idCount = new AtomicLong();

    private Long id;
    private String text;
    private LocalDateTime dueDate;
    private boolean done = false;
    private LocalDateTime doneDate;
    private Priority priority;
    private LocalDateTime creationDate;

    public Todo(String text, LocalDateTime dueDate, boolean done, LocalDateTime doneDate, Priority priority, LocalDateTime creationDate) {
        this.id = idCount.incrementAndGet();
        this.text = text;
        this.dueDate = dueDate;
        this.done = done;
        this.doneDate = doneDate;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    private enum Priority{
        HIGH,MEDIUM,LOW
    }

}

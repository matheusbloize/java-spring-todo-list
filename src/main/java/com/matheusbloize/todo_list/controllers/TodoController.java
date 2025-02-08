package com.matheusbloize.todo_list.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusbloize.todo_list.dtos.TodoRecordDto;
import com.matheusbloize.todo_list.models.TodoModel;
import com.matheusbloize.todo_list.services.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoModel>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.listAll());
    }

    @PostMapping
    public ResponseEntity<List<TodoModel>> create(@RequestBody @Valid TodoRecordDto todoDto) {
        var todo = new TodoModel();
        BeanUtils.copyProperties(todoDto, todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(todo));
    }

    @PutMapping("{id}")
    public ResponseEntity<List<TodoModel>> update(@PathVariable(name = "id") UUID id, @RequestBody @Valid TodoRecordDto todoDto) {
        var todo = new TodoModel();
        BeanUtils.copyProperties(todoDto, todo);
        return ResponseEntity.status(HttpStatus.OK).body(todoService.update(id, todo));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<List<TodoModel>> delete(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.delete(id));
    }
}

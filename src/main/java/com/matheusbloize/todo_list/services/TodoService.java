package com.matheusbloize.todo_list.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matheusbloize.todo_list.models.TodoModel;
import com.matheusbloize.todo_list.repositories.TodoRepository;

@Service
public class TodoService {
    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoModel> listAll() {
        return todoRepository.findAll();
    }

    public List<TodoModel> create(TodoModel todo) {
        todoRepository.save(todo);
        return listAll();
    }

    public List<TodoModel> update(UUID id, TodoModel todo) {
        Optional<TodoModel> optTodo = todoRepository.findById(id);
        if (optTodo.isPresent()) {
            todo.setId(id);
            todoRepository.save(optTodo.get());
        }
        return listAll();
    }

    public List<TodoModel> delete(UUID id) {
        Optional<TodoModel> optTodo = todoRepository.findById(id);
        if (optTodo.isPresent()) {
            todoRepository.delete(optTodo.get());
        }
        return listAll();
    }
}

package com.matheusbloize.todo_list.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusbloize.todo_list.models.TodoModel;

public interface TodoRepository extends JpaRepository<TodoModel, UUID> {

}

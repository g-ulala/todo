package com.example.todo.repository

import com.example.todo.entity.TodoItem
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<TodoItem, Long>

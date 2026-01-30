package com.example.todo.controller

import com.example.todo.entity.TodoItem
import com.example.todo.service.TodoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getTodos(): List<TodoItem> = todoService.getAllTodos()

    @PostMapping
    fun addTodo(@RequestBody request: Map<String, String>): TodoItem {
        val text = request["text"] ?: ""
        return todoService.addTodo(text)
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoUpdate: TodoItem): TodoItem {
        return todoService.updateTodo(id, todoUpdate.text, todoUpdate.isChecked)
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long) {
        todoService.deleteTodo(id)
    }
}

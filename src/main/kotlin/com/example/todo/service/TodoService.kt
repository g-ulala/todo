package com.example.todo.service

import com.example.todo.entity.TodoItem
import com.example.todo.repository.TodoRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class TodoService(private val repository: TodoRepository) {

    fun getAllTodos(): List<TodoItem> = repository.findAll()

    fun addTodo(text: String): TodoItem {
        val todo = TodoItem(text = text)
        return repository.save(todo)
    }

    fun updateTodo(id: Long, text: String, isChecked: Boolean): TodoItem {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }.also {
            it.text = text
            it.isChecked = isChecked
            repository.save(it)
        }
    }

    fun deleteTodo(id: Long) {
        if (!repository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }
        repository.deleteById(id)
    }
}

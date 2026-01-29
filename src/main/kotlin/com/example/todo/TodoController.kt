package com.example.todo

import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import jakarta.persistence.*

@Entity
@Table(name = "todos")
data class TodoItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var text: String,

    @Column(nullable = false)
    var isChecked: Boolean = false
)

@RestController
@RequestMapping("/api/todos")
class TodoController(private val repository: TodoRepository) {

    @GetMapping
    fun getTodos(): List<TodoItem> = repository.findAll()

    @PostMapping
    fun addTodo(@RequestBody request: Map<String, String>): TodoItem {
        val text = request["text"] ?: ""
        val todo = TodoItem(text = text)
        return repository.save(todo)
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoUpdate: TodoItem): TodoItem {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }.also {
            it.text = todoUpdate.text
            it.isChecked = todoUpdate.isChecked
            repository.save(it)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long) {
        if (!repository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }
        repository.deleteById(id)
    }
}
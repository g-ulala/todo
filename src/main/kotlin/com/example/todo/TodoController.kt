package com.example.todo

import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import jakarta.persistence.*
import java.util.concurrent.atomic.AtomicLong

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
class TodoController {
    private val todos = mutableListOf<TodoItem>()
    private val counter = AtomicLong()

    @GetMapping
    fun getTodos(): List<TodoItem> {
        return todos
    }

    @PostMapping
    fun addTodo(@RequestBody request: Map<String, String>): TodoItem {
        val text = request["text"] ?: ""
        val todo = TodoItem(counter.incrementAndGet(), text)
        todos.add(todo)
        return todo
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoUpdate: TodoItem): TodoItem? {
        val index = todos.indexOfFirst { it.id == id }
        if (index != -1) {
            todos[index] = todoUpdate
            return todos[index]
        }
        return null
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long) {
        todos.removeIf { it.id == id }
    }
}
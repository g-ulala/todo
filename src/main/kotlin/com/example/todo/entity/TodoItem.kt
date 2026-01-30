package com.example.todo.entity

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

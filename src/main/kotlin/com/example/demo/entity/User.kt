package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true)
    var email: String,

    var name: String,

    @Column(nullable = false, unique = true)
    var userHandle: String
)
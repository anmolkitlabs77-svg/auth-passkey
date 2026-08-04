package com.example.demo.repository

import com.example.demo.entity.Passkey
import com.example.demo.entity.User
import org.springframework.data.jpa.repository.JpaRepository
interface PasskeyRepository : JpaRepository<Passkey, Long> {

    fun findByCredentialId(
        credentialId: String
    ): Passkey?


    fun findAllByUser(
        user: User
    ): List<Passkey>


    fun findByCredentialIdAndUser(
        credentialId: String,
        user: User
    ): Passkey?

}


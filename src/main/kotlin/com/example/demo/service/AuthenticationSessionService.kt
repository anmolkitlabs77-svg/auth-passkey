package com.example.demo.service


import com.example.demo.session.AuthenticationSession
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthenticationSessionService {

    private val sessions =
        ConcurrentHashMap<String, AuthenticationSession>()

    fun save(session: AuthenticationSession) {
        sessions[session.email] = session
    }

    fun get(email: String): AuthenticationSession? {
        return sessions[email]
    }

    fun remove(email: String) {
        sessions.remove(email)
    }
}
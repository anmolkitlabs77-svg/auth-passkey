package com.example.demo.session

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RegistrationSessionService {

    private val sessions = ConcurrentHashMap<String, RegistrationSession>()

    fun save(session: RegistrationSession) {
        sessions[session.email] = session
    }

    fun get(email: String): RegistrationSession? {
        return sessions[email]
    }

    fun remove(email: String) {
        sessions.remove(email)
    }
}
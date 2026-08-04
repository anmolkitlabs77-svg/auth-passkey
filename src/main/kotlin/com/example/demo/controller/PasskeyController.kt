package com.example.demo.controller

import com.example.demo.dto.LoginStartRequest
import com.example.demo.dto.LoginVerifyRequest
import com.example.demo.dto.LoginVerifyResponse
import com.example.demo.dto.RegisterRequest
import com.example.demo.dto.RegisterVerifyRequest
import com.example.demo.service.PasskeyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/passkey")
class PasskeyController(
    private val passkeyService: PasskeyService
) {

    @PostMapping("/register/options")
    fun registerOptions(
        @RequestBody request: RegisterRequest
    ): String {

        return passkeyService.startRegistration(request)
    }

@PostMapping("/register/verify")
fun registerVerify(
    @RequestBody request: RegisterVerifyRequest
): ResponseEntity<LoginVerifyResponse> {

    passkeyService.finishRegistration(request)

    return ResponseEntity.ok(LoginVerifyResponse("Passkey Registered Successfully"))
}
    @PostMapping("/login/start")
    fun startLogin(
        @RequestBody request: LoginStartRequest
    ): ResponseEntity<String> {

        return ResponseEntity.ok(
            passkeyService.startAuthentication(request)
        )
    }

    @PostMapping("/login/verify")
    fun verifyLogin(
        @RequestBody request: LoginVerifyRequest
    ): ResponseEntity<LoginVerifyResponse> {

//        return ResponseEntity.ok(
//            LoginVerifyResponse("Login Successful")
//        )

        return ResponseEntity.ok(
            passkeyService.finishAuthentication(request)
        )
    }
}
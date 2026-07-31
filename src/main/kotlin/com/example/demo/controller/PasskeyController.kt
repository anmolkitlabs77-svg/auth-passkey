package com.example.demo.controller

import com.example.demo.dto.LoginRequest
import com.example.demo.dto.RegisterOptionsResponse
import com.example.demo.dto.RegisterRequest
import com.example.demo.dto.RegisterVerifyRequest
import com.example.demo.service.PasskeyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
/*

@RestController
@RequestMapping("/passkey")
class PasskeyController {


    @PostMapping("/register/options")
    fun registerOptions(
        @RequestBody request: RegisterRequest
    ):Any {


        return mapOf(

            "challenge" to "random_challenge",

            "rp" to mapOf(
                "name" to "My App",
                "id" to "localhost"
            ),

            "user" to mapOf(
                "id" to "123",
                "name" to request.email,
                "displayName" to request.name
            )
        )
    }


    @PostMapping("/register/verify")
    fun registerVerify(
        @RequestBody request:VerifyRequest
    ):Any {


        return mapOf(
            "success" to true
        )

    }


    @PostMapping("/login/options")
    fun loginOptions(
        @RequestBody request: LoginRequest
    ):Any {


        return mapOf(
            "challenge" to "login_challenge"
        )
    }



    @PostMapping("/login/verify")
    fun loginVerify(
        @RequestBody request: VerifyRequest
    ):Any {


        return mapOf(

            "success" to true,

            "token" to "jwt_token"

        )
    }

}*/


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
): ResponseEntity<String> {

    passkeyService.finishRegistration(request)

    return ResponseEntity.ok("Passkey Registered Successfully")
}

}
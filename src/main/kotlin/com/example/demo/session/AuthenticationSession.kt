package com.example.demo.session


import com.yubico.webauthn.AssertionRequest
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions

data class AuthenticationSession(

    val email: String,

    val request: AssertionRequest
)
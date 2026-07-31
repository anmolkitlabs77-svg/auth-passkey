package com.example.demo.session

import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions

data class RegistrationSession(

    val email: String,

    val userHandle: String,

    val options: PublicKeyCredentialCreationOptions

)
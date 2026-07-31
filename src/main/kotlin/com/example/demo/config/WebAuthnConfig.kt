package com.example.demo.config

import org.springframework.context.annotation.Configuration
import com.yubico.webauthn.data.RelyingPartyIdentity
import org.springframework.context.annotation.Bean

import com.example.demo.repository.CredentialRepositoryImpl
import com.yubico.webauthn.RelyingParty

@Configuration
class WebAuthnConfig {

    @Bean
    fun relyingPartyIdentity(): RelyingPartyIdentity =
        RelyingPartyIdentity.builder()
            .id("difficult-uneatable-ebay.ngrok-free.dev")
            .name("Passkey Demo")
            .build()

    @Bean
    fun relyingParty(
        credentialRepository: CredentialRepositoryImpl,
        relyingPartyIdentity: RelyingPartyIdentity
    ): RelyingParty {

        return RelyingParty.builder()
            .identity(relyingPartyIdentity)
            .credentialRepository(credentialRepository)
            .origins(setOf(
                "android:apk-key-hash:BfQ57ec9YJnPI2Az1cW-_VVv1fMIue2FHQws8bMylCQ"))
            .build()
    }
}


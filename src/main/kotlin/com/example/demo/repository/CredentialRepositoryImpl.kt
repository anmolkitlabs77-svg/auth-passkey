package com.example.demo.repository


import com.yubico.webauthn.CredentialRepository
import com.yubico.webauthn.RegisteredCredential
import com.yubico.webauthn.data.ByteArray
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor
import org.springframework.stereotype.Component
import java.util.Optional



@Component
class CredentialRepositoryImpl(
    private val userRepository: UserRepository,
    private val passkeyRepository: PasskeyRepository
) : CredentialRepository {

    override fun getCredentialIdsForUsername(username: String?): Set<PublicKeyCredentialDescriptor> {

        if (username == null) {
            return emptySet()
        }

        val user = userRepository.findByEmail(username)
            ?: return emptySet()

        return passkeyRepository
            .findAllByUser(user)
            .map {

                PublicKeyCredentialDescriptor.builder()
                    .id(ByteArray.fromBase64Url(it.credentialId))
                    .build()

            }
            .toSet()
    }
    override fun getUserHandleForUsername(
        username: String?
    ): Optional<ByteArray> {
        return Optional.empty()
    }

    override fun getUsernameForUserHandle(
        userHandle: ByteArray?
    ): Optional<String> {
        return Optional.empty()
    }

    override fun lookup(
        credentialId: ByteArray?,
        userHandle: ByteArray?
    ): Optional<RegisteredCredential> {
        return Optional.empty()
    }

    override fun lookupAll(
        credentialId: ByteArray?
    ): Set<RegisteredCredential> {
        return emptySet()
    }

}
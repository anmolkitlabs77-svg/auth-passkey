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
//    override fun getUserHandleForUsername(
//        username: String?
//    ): Optional<ByteArray> {
//        return Optional.empty()
//    }
    override fun getUserHandleForUsername(username: String?): Optional<ByteArray> {

    if (username == null) {
        return Optional.empty()
    }

    val user = userRepository.findByEmail(username)
        ?: return Optional.empty()

    return Optional.of(
        ByteArray.fromBase64Url(user.userHandle)
    )
}

//    override fun getUsernameForUserHandle(
//        userHandle: ByteArray?
//    ): Optional<String> {
//        return Optional.empty()
//    }

    override fun getUsernameForUserHandle(userHandle: ByteArray?): Optional<String> {

        if (userHandle == null) {
            return Optional.empty()
        }

        val encodedHandle = userHandle.base64Url

        val user = userRepository.findByUserHandle(encodedHandle)
            ?: return Optional.empty()

        return Optional.of(user.email)
    }

//    override fun lookup(
//        credentialId: ByteArray?,
//        userHandle: ByteArray?
//    ): Optional<RegisteredCredential> {
//        return Optional.empty()
//    }

    override fun lookup(
        credentialId: ByteArray?,
        userHandle: ByteArray?
    ): Optional<RegisteredCredential> {

        if (credentialId == null || userHandle == null) {
            return Optional.empty()
        }

        val user = userRepository.findByUserHandle(userHandle.base64Url)
            ?: return Optional.empty()

        val passkey = passkeyRepository
            .findByCredentialIdAndUser(
                credentialId.base64Url,
                user
            ) ?: return Optional.empty()

        return Optional.of(
            RegisteredCredential.builder()
                .credentialId(ByteArray.fromBase64Url(passkey.credentialId))
                .userHandle(ByteArray.fromBase64Url(user.userHandle))
                .publicKeyCose(ByteArray(passkey.publicKeyCose))
                .signatureCount(passkey.signatureCount)
                .build()
        )
    }


//    override fun lookupAll(
//        credentialId: ByteArray?
//    ): Set<RegisteredCredential> {
//        return emptySet()
//    }
    override fun lookupAll(
    credentialId: ByteArray?
    ): Set<RegisteredCredential> {

    if (credentialId == null) {
        return emptySet()
    }

    val passkey = passkeyRepository.findByCredentialId(credentialId.base64Url)
        ?: return emptySet()

    val user = passkey.user

    return setOf(
        RegisteredCredential.builder()
            .credentialId(ByteArray.fromBase64Url(passkey.credentialId))
            .userHandle(ByteArray.fromBase64Url(user.userHandle))
            .publicKeyCose(ByteArray(passkey.publicKeyCose))
            .signatureCount(passkey.signatureCount)
            .build()
    )
}

}


package com.example.demo.service


import com.example.demo.dto.LoginStartRequest
import com.example.demo.dto.LoginVerifyRequest
import com.example.demo.dto.RegisterRequest
import com.example.demo.dto.RegisterVerifyRequest
import com.example.demo.entity.Passkey
import com.example.demo.entity.User
import com.example.demo.repository.PasskeyRepository
import com.example.demo.repository.UserRepository
import com.example.demo.session.AuthenticationSession
import com.example.demo.session.RegistrationSession
import com.example.demo.session.RegistrationSessionService
import com.yubico.webauthn.FinishAssertionOptions
import com.yubico.webauthn.FinishRegistrationOptions
import com.yubico.webauthn.RelyingParty
import com.yubico.webauthn.StartAssertionOptions
import com.yubico.webauthn.StartRegistrationOptions
import com.yubico.webauthn.data.UserIdentity
import org.springframework.stereotype.Service
import java.security.SecureRandom
import com.yubico.webauthn.data.ByteArray
import com.yubico.webauthn.data.PublicKeyCredential
@Service
class PasskeyService(
    private val relyingParty: RelyingParty,
    private val registrationSessionService: RegistrationSessionService,
    private val authenticationSessionService: AuthenticationSessionService,
    private val userRepository: UserRepository,
    private val passkeyRepository: PasskeyRepository) {
    private val secureRandom = SecureRandom()
fun startRegistration(request: RegisterRequest): String {

    var user = userRepository.findByEmail(request.email)

    if (user == null) {

        val newHandle = generateUserHandle()

        user = User(
            email = request.email,
            name = request.name,
            userHandle = newHandle.base64Url
        )

        userRepository.save(user)
    }

    val userHandle =
        ByteArray.fromBase64Url(user.userHandle)

    val userIdentity =
        UserIdentity.builder()
            .name(user.email)
            .displayName(user.name)
            .id(userHandle)
            .build()

    val options =
        StartRegistrationOptions.builder()
            .user(userIdentity)
            .build()

    val creationOptions = relyingParty.startRegistration(options)

    registrationSessionService.save(
        RegistrationSession(
            email = user.email,
            userHandle = user.userHandle,
            options = creationOptions
        )
    )

    return creationOptions.toCredentialsCreateJson()
}
    private fun generateUserHandle(): ByteArray {

        val rawBytes = kotlin.ByteArray(32)

        secureRandom.nextBytes(rawBytes)

        return com.yubico.webauthn.data.ByteArray(rawBytes)

    }
    fun finishRegistration(request: RegisterVerifyRequest) {
        val session = registrationSessionService.get(request.email)
                ?: throw RuntimeException("Registration session not found")

        val credential = PublicKeyCredential.parseRegistrationResponseJson(request.credential)

        val finishOptions = FinishRegistrationOptions.builder()
                .request(session.options)
                .response(credential)
                .build()

        val result = relyingParty.finishRegistration(finishOptions)

        val user = userRepository.findByEmail(request.email) ?: throw RuntimeException("User not found")

        val passkey = Passkey(
            user = user,
            credentialId = result.keyId.id.base64Url,
            publicKeyCose = result.publicKeyCose.bytes,
            signatureCount = result.signatureCount,
            transports = null
        )

        passkeyRepository.save(passkey)
        registrationSessionService.remove(request.email)
    }
    fun startAuthentication(request: LoginStartRequest): String {

        val assertionRequest =
            relyingParty.startAssertion(
                StartAssertionOptions.builder()
                    .username(request.email)
                    .build()
            )

        authenticationSessionService.save(
            AuthenticationSession(
                email = request.email,
                request = assertionRequest
            )
        )

        return assertionRequest.toCredentialsGetJson()
    }
    fun finishAuthentication(request: LoginVerifyRequest): String {

        val session =
            authenticationSessionService.get(request.email)
                ?: throw RuntimeException("Authentication session not found")

        val credential =
            PublicKeyCredential.parseAssertionResponseJson(request.credential)

        val finishOptions =
            FinishAssertionOptions.builder()
                .request(session.request)
                .response(credential)
                .build()

        val result = relyingParty.finishAssertion(finishOptions)

        if (!result.isSuccess) {
            throw RuntimeException("Authentication failed")
        }

        authenticationSessionService.remove(request.email)

        return "Login Successful"
    }
}
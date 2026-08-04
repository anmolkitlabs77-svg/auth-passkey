package com.example.demo.entity

import jakarta.persistence.*


@Entity
@Table(name = "passkeys")
class Passkey(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false, unique = true, length = 512)
    var credentialId: String,


    @Column(name = "public_key_cose", columnDefinition = "BYTEA", nullable = false)
    var publicKeyCose: ByteArray,

    @Column(nullable = false)
    var signatureCount: Long,

    var transports: String? = null
)
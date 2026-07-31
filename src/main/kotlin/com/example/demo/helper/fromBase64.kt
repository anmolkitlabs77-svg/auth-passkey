package com.example.demo.helper

import com.yubico.webauthn.data.ByteArray

private fun fromBase64(value: String): ByteArray {
    return ByteArray.fromBase64Url(value)
}
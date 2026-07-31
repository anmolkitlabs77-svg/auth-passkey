package com.example.demo.helper

import com.yubico.webauthn.data.ByteArray

private fun toBase64(byteArray: ByteArray): String {
    return byteArray.base64Url
}
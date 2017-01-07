package org.putraxor.instagram

import java.math.BigInteger

object Crypto {

    /**
     * MD5 Function
     */
    fun md5(s: String): String {
        try {
            val m = java.security.MessageDigest.getInstance("MD5")
            m.update(s.toByteArray(), 0, s.length)
            return java.math.BigInteger(1, m.digest()).toString(16)
        } catch (e: Exception) {
            System.err.println("Error MD5" + e)
            return ""
        }

    }

    /**
     * HMAC Function
     */
    fun hmac256(key: String, data: String): String {
        try {
            val sha256_HMAC = javax.crypto.Mac.getInstance("HmacSHA256")
            val secret_key = javax.crypto.spec.SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
            sha256_HMAC.init(secret_key)

            val bytes = sha256_HMAC.doFinal(data.toByteArray(charset("UTF-8")))
            return java.lang.String.format("%040x", BigInteger(1, bytes))
        } catch (e: Exception) {
            System.err.println("Error hmac256 " + e)
            return ""
        }

    }

    /**
     * UUID v4 Function
     */
    fun randomUUID(type: Boolean): String {
        var uuid = java.util.UUID.randomUUID().toString()
        if (!type) {
            uuid = uuid.replace("-", "")
        }
        return uuid
    }

    /**
     * Signature Function
     */
    fun signData(payload: String): Signature {
        val signed = Crypto.hmac256(KEY.SIG_KEY, payload)
        return Signature(signed, KEY.APP_VERSION, KEY.SIG_VERSION, payload)
    }

    /**
     * Signature Data Class
     */
    data class Signature(val signed: String, val appVersion: String, val sigKeyVersion: String, val payload: String)


    fun generateDeviceId(username: String): String {
        var seed = 11111 + (Math.random() * ((99999 - 11111) + 1))
        var hash = md5("$username$seed")
        return "android-${hash.substring(0, 16)}"
    }


    /**
     * Main function for testing
     */
    @JvmStatic fun main(args: Array<String>) {
        //println(Crypto.md5("Tes MD5"))
        //println(Crypto.hmac256("12345", "Pesan HMAC"))
        //println(Crypto.randomUUID(false))
        println(Crypto.signData("""{"_csrftoken":"missing","device_id":"android-c3681fcd45f809bf","_uuid":"29e51ede-a5c9-4994-aeb4-3264c9fc03eb","username":"pratulisian","password":"qwertypoi","login_attempt_count":0}"""))
    }
}


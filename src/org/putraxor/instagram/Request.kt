package org.putraxor.instagram

import khttp.get
import khttp.post
import khttp.responses.Response
import khttp.structures.cookie.CookieJar

class Request {
    var url = "";
    var data = "";
    var isGet = true
    var persistedCookies: CookieJar = CookieJar()
    var headers = HTTP.HEADERS

    fun prepare(reqUrl: String, jsonString: String = ""): Request {
        url = "${HTTP.ENDPOINT}$reqUrl"
        //url = "http://localhost/cookies?fuck=${url}";
        data = jsonString
        isGet = data?.isNullOrEmpty()
        return this
    }


    fun send(): Response {
        //println("Cookies for ${url} is ${persistedCookies?.size} -> ${persistedCookies?.entries}")
        var resp: Response
        if (isGet) {
            if (persistedCookies == null) {
                resp = get(url, headers = headers)
            } else {
                resp = get(url, headers = headers, cookies = persistedCookies)
            }
        } else {
            val signature = data?.let { Crypto.signData(data) }
            val payload = mapOf("signed_body" to "${signature?.signed}.${signature?.payload}", "ig_sig_key_version" to signature?.sigKeyVersion)
            //println("Request.kt PayLoad = $payload")
            if (persistedCookies == null) {
                resp = post(url, headers = headers, data = payload)
            } else {
                resp = post(url, headers = headers, data = payload, cookies = persistedCookies)
            }
        }
        if (persistedCookies == null) {
            persistedCookies = resp.cookies
        } else {
            persistedCookies.putAll(resp.cookies)
        }
        return resp
    }
}
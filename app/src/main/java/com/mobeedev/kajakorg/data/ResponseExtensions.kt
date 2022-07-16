package com.mobeedev.kajakorg.data

import com.mobeedev.kajakorg.domain.exception.KajakApiException
import com.mobeedev.kajakorg.domain.exception.KajakException
import retrofit2.Response

fun <A : Any> Response<A>.bodyOrException(): A {
    val body = body()
    return if (isSuccessful && body != null) {
        body
    } else {
        throw KajakApiException(this)
    }
}
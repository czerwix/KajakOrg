package com.mobeedev.kajakorg.domain.error

import com.mobeedev.kajakorg.domain.exception.KajakApiException
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * Add all error mappers here or in specific modules
 */
internal fun KajakApiException.toBlockFiError(): KajakError = when {
  this.code == HttpURLConnection.HTTP_UNAUTHORIZED -> CoreError.Auth(this)
  this.code == HttpURLConnection.HTTP_NO_CONTENT -> CoreError.NoBody(this)
  this.code == HttpURLConnection.HTTP_BAD_METHOD -> CoreError.ServerError(this)
  this.code == HttpURLConnection.HTTP_BAD_REQUEST -> CoreError.ServerError(this)

  this.code >= HttpURLConnection.HTTP_INTERNAL_ERROR -> CoreError.ServerError(this)
  this.code >= HttpURLConnection.HTTP_UNAVAILABLE -> CoreError.ServiceUnavailable()
  else -> CoreError.ServerError(this)
}

internal fun HttpException.toBlockFiError(): KajakError = when (code()) {
  HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_UNAUTHORIZED -> CoreError.Auth()
  HttpURLConnection.HTTP_UNAVAILABLE -> CoreError.ServiceUnavailable()
  else -> CoreError.Unknown()
}

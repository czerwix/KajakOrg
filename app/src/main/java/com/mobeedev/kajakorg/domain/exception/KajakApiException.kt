package com.mobeedev.kajakorg.domain.exception

import com.mobeedev.kajakorg.domain.exception.KajakException
import retrofit2.Response

data class KajakApiException(
  val code: Int,
  val url: String,
  override val message: String?,
  override val originalException: Throwable? = null
) : KajakException() {

  // TODO [SK] Rebuild this to parse errorBody ass well when error codes are standardised
  constructor(response: Response<*>) : this(
    response.code(),
    response.raw().request.url.toString(),
    response.errorBody()?.string()
  )

  override fun toString() = "CODE: $code \nURL: $url \nMESSAGE: $message"
}

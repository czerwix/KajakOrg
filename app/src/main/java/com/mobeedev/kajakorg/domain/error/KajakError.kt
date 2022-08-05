package com.mobeedev.kajakorg.domain.error

import com.mobeedev.kajakorg.domain.exception.KajakException

abstract class KajakError : KajakException()

sealed class CoreError : KajakError() {
  data class Network(override val originalException: Throwable? = null) : CoreError()
  data class ServerError(override val originalException: Throwable? = null) : CoreError()
  data class ServiceUnavailable(override val originalException: Throwable? = null) : CoreError()
  data class NoBody(override val originalException: Throwable? = null) : CoreError()

  data class Auth(override val originalException: Throwable? = null) : CoreError()

  data class Unknown(override val originalException: Throwable? = null) : CoreError()
  data class ErrorWithDetail(val errorMsg: String, override val originalException: Throwable) : CoreError()
}

sealed class DataErrors : KajakError(){
  data class LastUpdateDateNotSet(val errorMsg: String, override val originalException: Throwable?= null) : DataErrors()
}
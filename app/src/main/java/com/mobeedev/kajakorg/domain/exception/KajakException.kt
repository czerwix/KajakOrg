package com.mobeedev.kajakorg.domain.exception

abstract class KajakException : Exception() {
  abstract val originalException: Throwable?
}

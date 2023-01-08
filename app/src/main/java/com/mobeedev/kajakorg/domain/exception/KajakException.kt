package com.mobeedev.kajakorg.domain.exception

abstract class KajakException : Exception() {
    abstract val originalException: Throwable?
}


data class NoPathDataException(
    override val message: String?,
    override val originalException: Throwable? = null
) : KajakException()


package com.mobeedev.kajakorg.domain.error

import android.util.Log
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onFailure(action: (exception: KajakError) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let {
        if (it is KajakError) {
            action(it)
        }
    }
    return this
}

inline fun <T, R> T.runRecoverCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(ErrorRegistry.handleErrorOrThrow(e))
    }
}
package com.mobeedev.kajakorg.domain.usecase.comon

import com.mobeedev.kajakorg.domain.schedulers.DefaultSchedulers
import com.mobeedev.kajakorg.domain.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.CoroutineContext

/**
 * Base use case class with parameters, handles the coroutine context switching
 * It also allows you to scope it to required job (most of the time viewmodel's)
 *
 * @param schedulers - [DefaultSchedulers] as default value,
 * to switch schedulers, pass the proper ones through the constructor
 * ```
 *    class LoginUseCase(
 *        schedulers: Schedulers,
 *        ...
 *    ) : UseCase<User, LoginUseCase.Params>(
 *        schedulers = schedulers
 *    )
 * ```
 */
abstract class WorkStatusExposedUseCase<out Type, in Params, StatusType>(
    schedulers: Schedulers = DefaultSchedulers()
) where Type : Any {
    var backgroundContext: CoroutineContext = schedulers.background()
    var foregroundContext: CoroutineContext = schedulers.main()

    private var _workStatus = MutableStateFlow<StatusType?>(null)
    var workStatus = _workStatus.asStateFlow()

    abstract suspend fun run(
        params: Params,
        workStatusFlow: MutableStateFlow<StatusType?>
    ): Result<Type>

    /**
     * Invokes the use case
     * You can scope the work by passing the coroutine job from parent
     *
     * @param parentJob - parent coroutine Job, useful when you are calling this from ViewModel
     */
    operator fun invoke(
        parentJob: Job = Job(),
        params: Params,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        CoroutineScope(backgroundContext + parentJob).launch {
            val result = run(params, _workStatus)
            withContext(foregroundContext) {
                onResult(result)
            }
        }
    }
}

fun <T : WorkStatusExposedUseCase<*, *, *>> T.makeUnconfined() = apply {
    backgroundContext = Dispatchers.Unconfined
    foregroundContext = Dispatchers.Unconfined
}

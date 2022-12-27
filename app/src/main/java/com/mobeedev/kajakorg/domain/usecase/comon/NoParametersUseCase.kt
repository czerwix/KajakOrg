package com.mobeedev.kajakorg.domain.usecase.comon

import com.mobeedev.kajakorg.domain.schedulers.DefaultSchedulers
import com.mobeedev.kajakorg.domain.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Base use case class with no parameters, handles the coroutine context switching
 * It also allows you to scope it to required job/context (most of the time viewmodel's)
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
abstract class NoParametersUseCase<Type>(
  schedulers: Schedulers = DefaultSchedulers()
) where Type : Any {
  var backgroundContext: CoroutineContext = schedulers.background()
  var foregroundContext: CoroutineContext = schedulers.main()

  abstract suspend fun run(): Result<Type>

  /**
   * @see WorkStatusExposedUseCase.invoke
   */
  operator fun invoke(parentJob: Job = Job(), onResult: (Result<Type>) -> Unit = {}) {
    val combinedScope = CoroutineScope(backgroundContext + parentJob)

    combinedScope.launch {
      val result = run()
      withContext(foregroundContext) {
        onResult(result)
      }
    }
  }

  fun <T : NoParametersUseCase<*>> T.makeUnconfined() = apply {
    backgroundContext = Dispatchers.Unconfined
    foregroundContext = Dispatchers.Unconfined
  }
}

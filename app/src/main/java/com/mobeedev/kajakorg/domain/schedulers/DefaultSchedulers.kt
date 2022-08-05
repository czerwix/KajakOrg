package com.mobeedev.kajakorg.domain.schedulers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultSchedulers : Schedulers {
  override fun io(): CoroutineDispatcher = Dispatchers.IO
  override fun background(): CoroutineDispatcher = Dispatchers.Default
  override fun main(): CoroutineDispatcher = Dispatchers.Main
}

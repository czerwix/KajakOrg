package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase
import java.time.ZonedDateTime

class GetLastUpdateDateUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<ZonedDateTime>() {

    override suspend fun run(): Result<ZonedDateTime> = kayakPathRepository.getLastUpdateDate()
}
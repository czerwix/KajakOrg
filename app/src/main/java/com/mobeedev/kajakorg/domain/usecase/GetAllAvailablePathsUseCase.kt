package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.data.KayakPathRepositoryImpl
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository

class GetAllAvailablePathsUseCase(
    private val kayakPathRepository: KayakPathRepository
) {
}
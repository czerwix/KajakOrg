package com.mobeedev.kajakorg.domain.repository

interface KayakPathRepository {

    suspend fun loadAllAvailablePaths(): Result<String>
}
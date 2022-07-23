package com.mobeedev.kajakorg.domain.usecase

import android.util.Log
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import java.lang.Exception

class GetAllPathsDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    UseCase<Boolean, GetAllPathsDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Result<Boolean> =try {
        kayakPathRepository.loadAllPathsDetails(params.pathIds)
    }catch (e:Exception){
        Log.e("cosik---",e.toString())
        Result.failure<Boolean>(e)
    }


    data class Params(val pathIds: List<Int>)
}
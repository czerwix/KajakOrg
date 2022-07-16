package com.mobeedev.kajakorg.ui.di

import com.mobeedev.kajakorg.data.KayakPathRepositoryImpl
import com.mobeedev.kajakorg.data.datasource.local.LocalPathSource
import com.mobeedev.kajakorg.data.datasource.remote.RemotePathSource
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.GetAllAvailablePathsUseCase
import com.mobeedev.kajakorg.ui.MainViewModel
import com.mobeedev.kajakorg.ui.common.ModuleLoader
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


object MainModule : ModuleLoader() {

    override val modules: List<Module> =
        listOf(
            viewModelModule,
            useCaseModule,
            repositoryModule,
            dataSourceModule
        )

}

private val viewModelModule = module {
    viewModel() {
        MainViewModel(get(), getAllAvailablePathsUseCase = get())
    }
}

private val useCaseModule = module {
    factory { GetAllAvailablePathsUseCase(kayakPathRepository = get()) }
}

private val repositoryModule = module {
    single<KayakPathRepository> {
        KayakPathRepositoryImpl(
            remotePathSource = get(),
            localPathSource = get()
        )
    }
}

private val dataSourceModule = module {
    single { LocalPathSource(kajakPathDao = get()) }
    single { RemotePathSource(kajakOrgService = get()) }
}
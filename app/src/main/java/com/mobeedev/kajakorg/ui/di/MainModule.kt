package com.mobeedev.kajakorg.ui.di

import com.mobeedev.kajakorg.data.ChecklistRepositoryImpl
import com.mobeedev.kajakorg.data.KayakPathRepositoryImpl
import com.mobeedev.kajakorg.data.SharedPreferencesRepositoryImpl
import com.mobeedev.kajakorg.data.datasource.local.LocalPathSource
import com.mobeedev.kajakorg.data.datasource.remote.RemotePathSource
import com.mobeedev.kajakorg.domain.repository.ChecklistRepository
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.repository.SharedPreferencesRepository
import com.mobeedev.kajakorg.domain.usecase.DeleteChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.GetChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLastUpdateDateUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalAllPathDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalMapPathsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathOverviewItemUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathsOverviewUseCase
import com.mobeedev.kajakorg.domain.usecase.GetMapDetailsStateUseCase
import com.mobeedev.kajakorg.domain.usecase.GetPathDetailsScreenInfoUseCase
import com.mobeedev.kajakorg.domain.usecase.LoadAllAvailablePathsUseCase
import com.mobeedev.kajakorg.domain.usecase.LoadAllPathsDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.SaveMapDetailsStateUseCase
import com.mobeedev.kajakorg.domain.usecase.UpdateChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.UpdateGoogleMapStatusUseCase
import com.mobeedev.kajakorg.ui.MainDataLoadingViewModel
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.common.ModuleLoader
import com.mobeedev.kajakorg.ui.path.details.PathDetailsViewModel
import com.mobeedev.kajakorg.ui.path.map.details.PathDetailMapViewModel
import com.mobeedev.kajakorg.ui.path.map.overview.PathMapViewModel
import com.mobeedev.kajakorg.ui.path.overview.PathOverviewViewModel
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
    viewModel {
        MainDataLoadingViewModel(
            application = get(),
            loadAllAvailablePathsUseCase = get(),
            loadAllPathsDetailsUseCase = get(),
            getPathsOverviewUseCase = get(),
            getPathsDetailsUseCase = get(),
            getLastUpdateDateUseCase = get()
        )
    }

    viewModel {
        PathOverviewViewModel(
            application = get(),
            getPathsOverViewUseCase = get()
        )
    }

    viewModel {
        PathDetailsViewModel(
            application = get(),
            savedStateHandle = it.get(),
            getPathDetailsUseCase = get(),
            updateGoogleMapStatusUSeCase = get()
        )
    }

    viewModel {
        PathMapViewModel(
            application = get(),
            savedStateHandle = it.get(),
            getLocalMapPathsUseCase = get()
        )
    }

    viewModel {
        PathDetailMapViewModel(
            application = get(),
            savedStateHandle = it.get(),
            getPathDetailsUseCase = get(),
            saveMapDetailsBoundsUseCase = get()
        )
    }

    viewModel {
        ChecklistViewModel(
            application = get(),
            getChecklistUseCase = get(),
            updateChecklistUseCase = get(),
            deleteChecklistUseCase = get()
        )
    }
}

private val useCaseModule = module {
    factory { LoadAllAvailablePathsUseCase(kayakPathRepository = get()) }
    factory { LoadAllPathsDetailsUseCase(kayakPathRepository = get()) }

    factory { GetLocalPathsOverviewUseCase(kayakPathRepository = get()) }
    factory { GetLocalAllPathDetailsUseCase(kayakPathRepository = get()) }
    factory { GetLocalPathOverviewItemUseCase(kayakPathRepository = get()) }
    factory { GetLastUpdateDateUseCase(kayakPathRepository = get()) }
    factory {
        GetLocalPathDetailsUseCase(
            kayakPathRepository = get(),
            getMapDetailsStateUseCase = get()
        )
    }
    factory { GetLocalMapPathsUseCase(kayakPathRepository = get()) }
    factory {
        GetPathDetailsScreenInfoUseCase(
            kayakPathRepository = get(),
            sharedPreferencesRepository = get()
        )
    }
    factory { UpdateGoogleMapStatusUseCase(sharedPreferencesRepository = get()) }
    factory { GetChecklistUseCase(checklistRepository = get()) }
    factory { UpdateChecklistUseCase(checklistRepository = get()) }
    factory { DeleteChecklistUseCase(checklistRepository = get()) }

    factory { SaveMapDetailsStateUseCase(kayakPathRepository = get()) }
    factory { GetMapDetailsStateUseCase(kayakPathRepository = get()) }
}

private val repositoryModule = module {
    single<KayakPathRepository> {
        KayakPathRepositoryImpl(
            remotePathSource = get(),
            localPathSource = get(),
            context = get()

        )
    }
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(
            context = get()
        )
    }
    single<ChecklistRepository> { ChecklistRepositoryImpl(checklistDao = get()) }
}

private val dataSourceModule = module {
    single { LocalPathSource(kajakPathDao = get()) }
    single { RemotePathSource(kajakOrgService = get()) }
}
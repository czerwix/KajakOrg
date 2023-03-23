package com.mobeedev.kajakorg.ui.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.mobeedev.kajakorg.common.config.EnvironmentConfiguration
import com.mobeedev.kajakorg.common.extensions.dataStore
import com.mobeedev.kajakorg.data.datasource.remote.KajakOrgService
import com.mobeedev.kajakorg.data.db.KajakDB
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppModule {

    val dataModule = module {
        single { KajakDB.getInstance(androidContext()) }

        single { get<KajakDB>().kajakPathDao() }
        single { get<KajakDB>().checklistDao() }

        single<DataStore<Preferences>> { get<Context>().dataStore }
    }

    val configModule = module {
        single { EnvironmentConfiguration }
    }

    val sharedPreferencesModule = module {
        single {
            EncryptedSharedPreferences.create(
                get<EnvironmentConfiguration>().SHARED_PREFERENCES_FILE_NAME,
                get<EnvironmentConfiguration>().SHARED_PREFERENCES_MASTER_KEY_ALIAS,
                get(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    val remoteModule = module {
        single {
            val timeout = get<EnvironmentConfiguration>().maxTimeout

            OkHttpClient.Builder()
                .apply {
                    connectTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS)
                }
//                .addInterceptor(
//                    HttpLoggingInterceptor()
//                        .apply {
//                            //todo remove if not in debug flavour
//                            level = HttpLoggingInterceptor.Level.BODY
//                        })
                .build()
        }

        single<Retrofit>() {
            Retrofit.Builder()
                .addConverterFactory(
                    TikXmlConverterFactory.create(
                        TikXml.Builder()
//                            .exceptionOnUnreadXml(false)
                            .build()
                    )
                )
                .baseUrl(get<EnvironmentConfiguration>().baseUrl)
                .client(get())
                .build()
        }
    }

    val apiModule = module {
        single { get<Retrofit>().create(KajakOrgService::class.java) }
    }
}
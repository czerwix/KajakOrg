package com.mobeedev.kajakorg

import androidx.multidex.MultiDexApplication
import com.mobeedev.kajakorg.ui.di.AppModule
import com.mobeedev.kajakorg.ui.di.MainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PaddlePalApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        setUpKoin()
    }

    private fun setUpKoin() {
        val modules = AppModule()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@PaddlePalApp)
            modules(
                modules.dataModule,
                modules.configModule,
                modules.sharedPreferencesModule,
                modules.remoteModule,
                modules.apiModule
            )

            MainModule.load()
        }
    }

    //For performance debug.
//    private fun setUpStrictMode() {
//        StrictMode.setThreadPolicy(
//            StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()
//                .detectCustomSlowCalls()
//                .detectAll()
//                .penaltyLog()
//                .build()
//        )
//        StrictMode.setVmPolicy(
//            StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .build()
//        )
//
//        StrictMode.noteSlowCall("Slow code")
//    }
}
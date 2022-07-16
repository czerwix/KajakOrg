package com.mobeedev.kajakorg.ui.common

import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class ModuleLoader {
    private var modulesLoaded = false
    abstract val modules: List<Module>

    /**
     * Loads koin modules specified in [modules]
     *
     * IMPORTANT: Call before ```super.onCreate()``` in an activity/fragment
     */
    fun load() {
        if (modulesLoaded.not()) {
            loadKoinModules(modules)
            modulesLoaded = true
            onLoad()
        }
    }

    fun unload() {
        if (modulesLoaded) {
            unloadKoinModules(modules)
            modulesLoaded = false
            onUnLoad()
        }
    }

    open fun onLoad() {}

    open fun onUnLoad() {}
}

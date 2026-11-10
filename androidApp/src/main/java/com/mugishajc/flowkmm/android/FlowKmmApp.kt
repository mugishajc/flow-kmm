package com.mugishajc.flowkmm.android

import android.app.Application
import com.mugishajc.flowkmm.data.local.DatabaseDriverFactory
import com.mugishajc.flowkmm.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class FlowKmmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlowKmmApp)
            modules(
                sharedModule,
                module {
                    single { DatabaseDriverFactory(get()) }
                },
            )
        }
    }
}

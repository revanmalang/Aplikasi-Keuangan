package com.besthora.moneytoringapp

import android.app.Application
import com.besthora.moneytoringapp.di.AllMoneyViewModelModule
import com.besthora.moneytoringapp.di.dataModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
@FlowPreview
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@BaseApplication)
            modules(
                dataModule,
                AllMoneyViewModelModule
            )
        }
    }
}
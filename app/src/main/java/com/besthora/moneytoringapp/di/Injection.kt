package com.besthora.moneytoringapp.di

import com.besthora.moneytoringapp.data.database.DatabaseMoney
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.data.repository.MoneyRepository
import com.besthora.moneytoringapp.data.repository.MoneyRepositoryImpl
import com.besthora.moneytoringapp.ui.list.AllMoneyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    single { DatabaseMoney.create(get()) }

    factory<MoneyRepository> {
        val notifyDao = get<DatabaseMoney>().MoneyDao()
        MoneyRepositoryImpl(notifyDao)
    }

    factory {
        PreferencesMoney(androidContext())
    }
}


val AllMoneyViewModelModule = module {
    viewModel {
        AllMoneyViewModel(get())
    }
}

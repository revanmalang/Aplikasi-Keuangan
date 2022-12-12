package com.besthora.moneytoringapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.besthora.moneytoringapp.ui.list.AllMoneyViewModel

class ViewModelFactory(context: Context) :
    ViewModelProvider.NewInstanceFactory() {

//    val StoryRepos = Injection.provideRepository(context)

//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AllMoneyViewModel::class.java)) {
//            return AllMoneyViewModel(StoryRepos) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//    }
}
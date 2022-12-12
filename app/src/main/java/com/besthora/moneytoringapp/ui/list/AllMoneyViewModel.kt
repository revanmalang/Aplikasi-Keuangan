package com.besthora.moneytoringapp.ui.list


import androidx.lifecycle.ViewModel
import com.besthora.moneytoringapp.data.repository.MoneyRepository
import com.besthora.moneytoringapp.service.TrxDataNotif
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow


@FlowPreview
@ExperimentalCoroutinesApi
class AllMoneyViewModel constructor(val repository: MoneyRepository) : ViewModel()  {

    fun getAllMoneyForHome(userId: String) : Flow<List<TrxDataNotif>> = repository.getAllMoneyForHome(userId)

    fun getAllMoneyForAny(userId: String) : Flow<List<TrxDataNotif>> = repository.getAllMoneyForAny(userId)

    fun getAllMoneyBytipe(filter: String,userId: String) : Flow<List<TrxDataNotif>> = repository.getAllMoneyBytipe(filter,userId)

    fun getAllMoneyByWhentrx(filter: String,userId: String) : Flow<List<TrxDataNotif>> = repository.getAllMoneyByWhentrx(filter,userId)
}
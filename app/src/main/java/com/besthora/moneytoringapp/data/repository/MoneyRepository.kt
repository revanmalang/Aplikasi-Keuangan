package com.besthora.moneytoringapp.data.repository

import com.besthora.moneytoringapp.service.TrxDataNotif
import kotlinx.coroutines.flow.Flow


interface MoneyRepository {

    fun insertTrx(
        id: String,
        userId: String,
        PackageName: String,
        IconApp: String,
        Title: String,
        NotifBody: String,
        Nominal: String,
        TimeStamp: String,
        Tipe : String,
        Whentrx: String)

    fun getAllMoneyForHome(userId: String): Flow<List<TrxDataNotif>>
    fun getAllMoneyBytipe(filter: String,userId: String): Flow<List<TrxDataNotif>>
    fun getAllMoneyByWhentrx(filter: String,userId: String): Flow<List<TrxDataNotif>>
    fun getAllMoneyForAny(userId: String): Flow<List<TrxDataNotif>>
}
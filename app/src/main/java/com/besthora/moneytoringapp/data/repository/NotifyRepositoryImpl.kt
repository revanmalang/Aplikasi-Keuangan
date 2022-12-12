package com.besthora.moneytoringapp.data.repository

import com.besthora.moneytoringapp.data.database.dao.MoneyDao
import com.besthora.moneytoringapp.service.TrxDataNotif
import kotlinx.coroutines.flow.Flow


class MoneyRepositoryImpl constructor(
    private val notifyDao: MoneyDao,
) : MoneyRepository {
     override fun insertTrx(
         id: String,
         userId: String,
         PackageName: String,
         IconApp: String,
         Title: String,
         NotifBody: String,
         Nominal: String,
         TimeStamp: String,
         Tipe : String,
         Whentrx: String){

         notifyDao.insertMoney(TrxDataNotif(
             id = id,
             userId = userId,
             PackageName = PackageName,
             IconApp = IconApp,
             Title = Title,
             NotifBody = NotifBody,
             Nominal = Nominal,
             TimeStamp= TimeStamp,
             Tipe= Tipe,
             Whentrx= Whentrx
         ))
    }


    override fun getAllMoneyForHome(userId: String): Flow<List<TrxDataNotif>>  = notifyDao.getAllMoneyForHome(userId)
    override fun getAllMoneyBytipe(filter: String,userId: String): Flow<List<TrxDataNotif>>  = notifyDao.getAllMoneyBytipe(filter,userId)
    override fun getAllMoneyByWhentrx(filter: String,userId: String): Flow<List<TrxDataNotif>>  = notifyDao.getAllMoneyByWhentrx(filter,userId)
    override fun getAllMoneyForAny(userId: String): Flow<List<TrxDataNotif>> = notifyDao.getAllMoneyForAny(userId)

}

package com.besthora.moneytoringapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.besthora.moneytoringapp.service.TrxDataNotif
import kotlinx.coroutines.flow.Flow


@Dao
interface MoneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoney(moneynotif: TrxDataNotif)

    @Query("SELECT * FROM tb_notif_money WHERE userId= :userid ORDER BY TimeStamp DESC LIMIT 5")
    fun getAllMoneyForHome( userid: String): Flow<List<TrxDataNotif>>

    @Query("SELECT * FROM tb_notif_money WHERE Tipe = :tipe AND userId= :userid ORDER BY TimeStamp DESC")
    fun getAllMoneyBytipe(tipe: String, userid: String): Flow<List<TrxDataNotif>>

    @Query("SELECT * FROM tb_notif_money WHERE Whentrx = :whentrx  AND userId= :userid ORDER BY TimeStamp DESC")
    fun getAllMoneyByWhentrx(whentrx: String, userid: String): Flow<List<TrxDataNotif>>

    @Query("SELECT * FROM tb_notif_money  WHERE userId= :userid ORDER BY TimeStamp DESC")
    fun getAllMoneyForAny(userid: String): Flow<List<TrxDataNotif>>

}

package com.besthora.moneytoringapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.besthora.moneytoringapp.BuildConfig
import com.besthora.moneytoringapp.data.database.dao.MoneyDao
import com.besthora.moneytoringapp.service.TrxDataNotif


@Database(
    entities = [TrxDataNotif::class],
    version = 2,
    exportSchema = false
)
abstract class DatabaseMoney : RoomDatabase() {

    abstract fun MoneyDao(): MoneyDao

    companion object Factory {
        fun create(context: Context): DatabaseMoney {
            return Room.databaseBuilder(context, DatabaseMoney::class.java, "dbmoney")
                .apply {
                    if (BuildConfig.DEBUG) {
                        fallbackToDestructiveMigration()
                    }
                }
                .allowMainThreadQueries()
                .build()
        }
    }
}
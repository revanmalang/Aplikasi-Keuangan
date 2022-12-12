package com.besthora.moneytoringapp.service
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
@Entity(tableName = "tb_notif_money")
data class TrxDataNotif(
    @PrimaryKey()
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("PackageName")
    val PackageName: String,

    @field:SerializedName("IconApp")
    val IconApp: String,

    @field:SerializedName("Title")
    val Title: String,

    @field:SerializedName("NotifBody")
    val NotifBody: String,

    @field:SerializedName("Nominal")
    val Nominal: String,

    @field:SerializedName("TimeStamp")
    val TimeStamp: String,

    @field:SerializedName("Tipe")
    val Tipe: String,

    @field:SerializedName("Whentrx")
    val Whentrx: String,

    ): Parcelable

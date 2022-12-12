package com.besthora.moneytoringapp.data.helper

import android.os.Build
import androidx.annotation.RequiresApi
import com.besthora.moneytoringapp.data.repository.MoneyRepository
import com.besthora.moneytoringapp.service.TrxNotificationListenerService.ApplicationPackageNames
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID
import kotlin.text.Charsets.UTF_8

class InsertTrxHelper(val title: String,
                      val text: String,
                      val packname: String,
                      val date: Long,
                      val userId: String,
                      val moneyRepository : MoneyRepository) {

    private var nominalfirst = 0


    @RequiresApi(Build.VERSION_CODES.N)
    fun detextApp(){
        when(packname){
            ApplicationPackageNames.BRIMO_PKGNAME -> checkOutorInBRIMO(text)
            ApplicationPackageNames.OVO_PKGNAME -> checkOutorInOVO(text)
            ApplicationPackageNames.SMS_PKGNAME -> checkOutorInSMS(text)
            ApplicationPackageNames.DANA_PKGNAME -> checkOutorInDANA(text)
        }
    }

    private fun checkOutorInDANA(text: String) {

    }

    private fun checkOutorInSMS(text: String) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkOutorInOVO(text: String) {

        detectAdminOvo(text)

        if ((text.contains("TOP UP") && text.contains("Top up") && !text.contains("dipotong"))
            || ((text.contains("Top up sebesar")) and text.contains("BANK BRI"))
            || (text.contains("mengirimkan dana") and text.contains("melalui aplikasi OVO"))){
            detectInOvo(text)
        }else if(
            text.contains("dipotong")
            || text.contains("Pembayaran di Merchant SMARTFRENT")
            || (text.contains("mengirimkan dana") && text.contains("melalui aplikasi OVO"))
        ){ detectOutOvo(text) }

    }

    private fun detectInOvo(text: String) {
        val notifIntOvo = text.split(" ")
        notifIntOvo.indices.map { data->
            if (notifIntOvo[data].contains(",00")
                || notifIntOvo[data].contains(".00")
                || notifIntOvo[data].contains("00"))  {
            nominalfirst = notifIntOvo[data]
                .replace("Rp","")
                .replace(".","").toInt() }
        }
        finalInsert(text, nominalfirst.toString(), "pemasukan")
    }

    private fun detectOutOvo(text: String) {
        val notifOutOvo = text.split(" ")
        notifOutOvo.indices.map { data->
            if (notifOutOvo[data].contains("00")
                || notifOutOvo[data].contains(".00")
                || notifOutOvo[data].contains(",00") )  {
            nominalfirst = notifOutOvo[data]
                .replace("Rp","")
                .replace(".","").toInt() }
        }
        finalInsert(text, nominalfirst.toString(), "pengeluaran")
    }


    private fun detectAdminOvo(text: String) {
        if(text.contains("Transfer") && text.contains("dikenakan biaya transaksi")){
            val twoNominalOut = text.split("berhasil.")

            twoNominalOut[0].split(" ").indices.map { data->
                if (twoNominalOut[0].split(" ")[data].contains(",00")
                || twoNominalOut[0].split(" ")[data].contains(".00")
                || twoNominalOut[0].split(" ")[data].contains("00") )  {
                val getlastindex = twoNominalOut[0]
                    .split(" ")[data]
                    .replace("Rp","")
                    .split(".")
                if (getlastindex.lastIndex ==2){
                    nominalfirst  = twoNominalOut[0]
                        .split(" ")[data]
                        .replace("Rp","")
                        .replace(".","")
                        .substring(0, twoNominalOut[0]
                            .split(" ")[data]
                            .replace("Rp","")
                            .replace(".","")
                            .toInt().toString().length - getlastindex.lastIndex).toInt()
                }else{
                    nominalfirst = twoNominalOut[0]
                        .split(" ")[data]
                        .replace("Rp","")
                        .replace(".","")
                        .replace(",","").toInt()
                }
            }
            }
            twoNominalOut[1].split(" ").indices.map { data->
                if (twoNominalOut[1].split(" ")[data].contains(",00")
                    || twoNominalOut[1].split(" ")[data].contains(".00")
                    || twoNominalOut[1].split(" ")[data].contains("00"))  {
                nominalfirst += twoNominalOut[1]
                    .split(" ")[data]
                    .replace("Rp","")
                    .replace(".","").toInt() }
            }

            finalInsert(text, nominalfirst.toString(), "pengeluaran")
        }
    }


    private fun checkOutorInBRIMO(text: String) {

        val notivBri = text.split(" ")
        val desc = text
        for (i in notivBri.indices){
            if(notivBri[i].contains("Rp") && notivBri[i].contains(".00")){
                if(
                    (text.contains("Pembayaran BRIVA")
                            || text.contains("Pembayaran Tagihan Listrik")
                            || text.contains("Transaksi Top Up OVO")
                            || text.contains("Transaksi Pembelian QRIS"))
                ){ // out
                    val moneytoBri = notivBri[i].replace("Rp", "")
                        .replace(",",".")
                    val ceklenglastindex = moneytoBri.split(".")
                    val realmoney = moneytoBri.replace(".","")
                    if (ceklenglastindex.lastIndex == 2){
                        finalInsert(desc, realmoney.substring(0, realmoney.length-ceklenglastindex.lastIndex) , "pengeluaran")
                    }else{
                        finalInsert(desc, notivBri[i].replace("Rp", ""), "pengeluaran")
                    }

                }else{
                    finalInsert(desc, notivBri[i].replace("Rp", ""), "pemasukan")
                }
            }
        }

    }
    fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
    fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

    fun finalInsert(desc : String, nominal: String, tipe: String){

        val idtrx: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(desc.toByteArray()+tipe.toByteArray())
        } else {
            "null"
        }

        moneyRepository.insertTrx(
            md5(idtrx+UUID.randomUUID()).toHex(),
            "${userId}",
            "${packname}",
            "",
            "${title}",
            "${desc}",
            "${nominal}",
            "${date}",
            "${tipe}",
            "online")
    }

}
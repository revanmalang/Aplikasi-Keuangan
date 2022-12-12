package com.besthora.moneytoringapp.ui.create

import android.text.Editable
import java.util.*

data class TransactionModel(
    var transactionID: String? =null,
    var type: Int? =null,
    var title: String? =null,
    var amount: Double? =null,
    var date: Long? =null,
    var note: String? =null,
    var invertedDate: Long?=null
)
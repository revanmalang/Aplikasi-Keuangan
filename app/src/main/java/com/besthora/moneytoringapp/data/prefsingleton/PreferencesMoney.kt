package com.besthora.moneytoringapp.data.prefsingleton

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesMoney constructor(
    private val context: Context
) {


    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var getmode: String?
        get() = prefs.getString(MODE_GUEST, null)
        set(value) {
            prefs.edit {
                putString(MODE_GUEST, value)
            }
        }

    var relog_guest: String?
        get() = prefs.getString(MODE_GUEST_RELOG, null)
        set(value) {
            prefs.edit {
                putString(MODE_GUEST_RELOG, value)
            }
        }
    var pemasukan: String?
        get() = prefs.getString(PEMASUKAN, null)
        set(value) {
            prefs.edit {
                putString(PEMASUKAN, value)
            }
        }
    var pengeluaran: String?
        get() = prefs.getString(PENGELUARAN, null)
        set(value) {
            prefs.edit {
                putString(PENGELUARAN, value)
            }
        }

    var tmpinsertapp: String?
        get() = prefs.getString(TMP_INSERT_APP, null)
        set(value) {
            prefs.edit {
                putString(TMP_INSERT_APP, value)
            }
        }

    var tmpinsertmode: String?
        get() = prefs.getString(TMP_INSERT_MODE, null)
        set(value) {
            prefs.edit {
                putString(TMP_INSERT_MODE, value)
            }
        }
    var tmpinsertipe: String?
        get() = prefs.getString(TMP_INSERT_TIPE, null)
        set(value) {
            prefs.edit {
                putString(TMP_INSERT_TIPE, value)
            }
        }

    var permissionnotif: String?
        get() = prefs.getString(PERMISSION_NOTIF, null)
        set(value) {
            prefs.edit {
                putString(PERMISSION_NOTIF, value)
            }
        }

    companion object {
        private const val MODE_GUEST = "gueston"
        private  const val PEMASUKAN = "PEMASUKAN"
        private  const val PENGELUARAN = "PENGELUARAN"
        private const val MODE_GUEST_RELOG = "relog"
        private const val TMP_INSERT_TIPE = "TMP_INSERT_TIPE"
        private const val TMP_INSERT_MODE = "TMP_INSERT_MODE"
        private const val TMP_INSERT_APP = "TMP_INSERT_APP"
        private const val PERMISSION_NOTIF = "PERMISSION_NOTIF"
    }
}


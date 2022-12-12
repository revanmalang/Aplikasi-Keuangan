package com.besthora.moneytoringapp.ui.list


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.databinding.ItemdatatrxBinding
import com.besthora.moneytoringapp.databinding.ItemdatatrxHomeBinding
import com.besthora.moneytoringapp.service.TrxDataNotif
import com.besthora.moneytoringapp.ui.helper.IntToDecimal
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class AdapterMoneyForAllMoney :
    ListAdapter<TrxDataNotif, AdapterMoneyForAllMoney.MoneyViewHolder>(diffCallback) {

    inner class MoneyViewHolder(val binding: ItemdatatrxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyViewHolder {
        return MoneyViewHolder(
            ItemdatatrxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MoneyViewHolder, position: Int) {
        val data = getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                val title = data.Title
                val desc = data.NotifBody
                val date = data.TimeStamp
                var poto_url: Drawable? = null

                when(data.PackageName){
                    "id.co.bri.brimo" -> {
                        poto_url = context.getResources().getDrawable(R.drawable.ic_logo_brimo_fix)
                    }
                    "ovo.id" -> {
                        poto_url = context.getResources().getDrawable(R.drawable.ic_logo_ovo_fix)
                    }
                    "id.dana" -> {
                        poto_url = context.getResources().getDrawable(R.drawable.ic_logo_dana_fix)
                    }
                    "com.android.mms" -> {
                        poto_url = context.getResources().getDrawable(R.drawable.ic_logo_sms_fix)
                    }
                    "manual.insert.data" -> {
                        poto_url = context.getResources().getDrawable(R.drawable.ic_baseline_monetization_on_24)
                    }


                }
                Glide.with(holder.itemView.context)
                    .load(
                        if (poto_url != null) poto_url else ""
                    )
                    .circleCrop()
                    .into(holder.binding.iconAppTrxHome)
                val nominal = data.Nominal

                holder.binding.bankHome.text = title

                val sdf = SimpleDateFormat("dd/MM/yy")
                val netDate = Date(date.toLong())
                val dates = sdf.format(netDate)
                holder.binding.dateTrx.text = dates
                holder.binding.descTrx.text = desc

                when(data.Tipe){
                    "pengeluaran" -> {
                        holder.binding.nominalHome.text = "- Rp${IntToDecimal.formatDecimalSeperators(nominal.replace("Rp", "").replace(".","").replace(",",""))  }"
                        holder.binding.nominalHome.setTextColor(getResources().getColor(
                            R.color.text_pengeluaran))
                    }
                    "pemasukan" -> {
                        holder.binding.nominalHome.text = "+ Rp${IntToDecimal.formatDecimalSeperators(nominal.replace("Rp", "").replace(".","").replace(",",""))  }"
                        holder.binding.nominalHome.setTextColor(getResources().getColor(
                            R.color.text_pemasukan))
                    }
                }
                if (data.Whentrx == "online") {
                    holder.binding.statusCard.setBackgroundColor(getResources().getColor(
                        R.color.text_pemasukan))
                }else{
                    holder.binding.statusCard.setBackgroundColor(getResources().getColor(
                        R.color.text_pengeluaran))
                }
            }
        }
    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<TrxDataNotif>() {
            override fun areItemsTheSame(oldItem: TrxDataNotif, newItem: TrxDataNotif): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: TrxDataNotif, newItem: TrxDataNotif): Boolean {
                return oldItem == newItem
            }
        }
    }


}
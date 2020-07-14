package com.android.misfinanzas.ui.movements

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.data.local.model.MovementVO
import com.android.data.remote.model.MovementType
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewHolder
import kotlinx.android.synthetic.main.movement_row.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MovementsAdapter(
    private val context: Context,
    private val movements: List<MovementVO>,
    private val itemClickListener: OnMovementClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface OnMovementClickListener {
        fun onMovementClicked(movement: MovementVO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MovementsViewHolder(LayoutInflater.from(context).inflate(R.layout.movement_row, parent, false))
    }

    override fun getItemCount(): Int {
        return movements.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MovementsViewHolder -> holder.bind(movements[position], position)
            //put other views here if you can
        }
    }

    //inner class to avoid memory leak, when parent class died (MovementsAdapter) this inner also die
    inner class MovementsViewHolder(itemView: View) : BaseViewHolder<MovementVO>(itemView) {

        private var moneyFormat: NumberFormat = DecimalFormat("$ ###,###,##0.00")
        private var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        override fun bind(item: MovementVO, position: Int) {
            when (item.idType) {
                MovementType.CASH_INCOME -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_cash_income)
                }
                MovementType.CASH_OUT -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_cash_out)
                }
                MovementType.CARD_INCOME -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_card_income)
                }
                MovementType.CARD_OUT -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_card_out)
                }
                MovementType.WITHDRAWAL -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_transfer)
                }
                MovementType.CREDIT_CARD_BUY -> {
                    itemView.iv_tipo_movimiento.setImageResource(R.mipmap.ic_credit_card_buy)
                }
            }
            itemView.tv_valor.text = moneyFormat.format(item.value)
            itemView.tv_fecha.text = dateFormat.format(item.date!!)
            itemView.tv_descripcion.text = item.description

            itemView.setOnClickListener { itemClickListener.onMovementClicked(item) }
        }
    }
}

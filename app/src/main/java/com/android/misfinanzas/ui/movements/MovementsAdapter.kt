package com.android.misfinanzas.ui.movements

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.data.remote.model.Movement
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewHolder
import kotlinx.android.synthetic.main.movement_row.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

class MovementsAdapter(private val context: Context, private val movements: List<Movement>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

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
    inner class MovementsViewHolder(itemView: View) : BaseViewHolder<Movement>(itemView) {
        var formatter: NumberFormat = DecimalFormat("###,###,##0.00")
        override fun bind(item: Movement, position: Int) {
            itemView.tv_valor.text = "$ " + formatter.format(item.Valor)
            itemView.tv_descripcion.text = item.Descripcion
        }
    }
}

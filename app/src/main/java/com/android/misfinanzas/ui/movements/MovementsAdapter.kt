package com.android.misfinanzas.ui.movements

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.data.UserSesion
import com.android.data.local.model.MovementVO
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewHolder
import com.android.misfinanzas.base.MovementType
import kotlinx.android.synthetic.main.movement_row.view.*

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
        override fun bind(item: MovementVO, position: Int) {
            itemView.iv_tipo_movimiento.setImageResource((MovementType.getImage(item.idType)))
            itemView.tv_valor.text = UserSesion.getMoneyFormat().format(item.value)
            itemView.tv_fecha.text = UserSesion.getDateFormat().format(item.date!!)
            itemView.tv_descripcion.text = item.description

            itemView.setOnClickListener { itemClickListener.onMovementClicked(item) }
        }
    }
}

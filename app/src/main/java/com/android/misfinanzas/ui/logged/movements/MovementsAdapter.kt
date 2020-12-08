package com.android.misfinanzas.ui.logged.movements

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.model.Movement
import com.android.domain.utils.DateUtils
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewHolder
import com.android.misfinanzas.base.MovementClickListener
import com.android.misfinanzas.base.MovementType
import com.android.misfinanzas.databinding.RowMovementBinding

class MovementsAdapter(
    private val context: Context,
    private val movements: List<Movement>,
    private val itemClickListener: MovementClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MovementsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_movement, parent, false))
    }

    override fun getItemCount(): Int {
        return movements.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MovementsViewHolder -> holder.bind(movements[position], position)
            //put other views here if you want
        }
    }

    //inner class to avoid memory leak, when parent class died (MovementsAdapter) this inner also die
    inner class MovementsViewHolder(itemView: View) : BaseViewHolder<Movement>(itemView) {

        val binding = RowMovementBinding.bind(itemView)

        override fun bind(item: Movement, position: Int) = with(binding) {
            ivTipoMovimiento.setImageResource((MovementType.getImage(item.idType)))
            tvValor.text = MoneyUtils.getMoneyFormat().format(item.value)
            tvFecha.text = DateUtils.getDateFormat().format(item.date!!)
            tvDescripcion.text = item.description

            itemView.setOnClickListener { itemClickListener.onMovementClicked(item) }

            //TODO pending to add category description to model
            tvCategoria.visibility = View.GONE

            if (item.idMovement < 0) {
                ibDiscard.visibility = View.VISIBLE
                ibDiscard.setOnClickListener { itemClickListener.onDiscardMovementClicked(item.idMovement, position) }
            } else {
                ibDiscard.visibility = View.GONE
            }
        }
    }
}

package com.android.misfinanzas.ui.logged.movements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.utils.DateUtils
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.R
import com.android.misfinanzas.base.MovementType
import com.android.misfinanzas.databinding.RowMovementBinding
import com.android.misfinanzas.models.MovementModel
import com.android.misfinanzas.utils.gone
import com.android.misfinanzas.utils.visible

class MovementsAdapter : ListAdapter<MovementModel, MovementsAdapter.ViewHolder>(DiffCallback) {

    private var listener: OnActionItemListener? = null

    fun setOnActionItemListener(listener: OnActionItemListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowMovementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<MovementModel>() {
        override fun areItemsTheSame(oldItem: MovementModel, newItem: MovementModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovementModel, newItem: MovementModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: RowMovementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MovementModel) = with(binding) {
            ivMovType.setImageResource((MovementType.getImage(model.idType)))
            tvValor.text = MoneyUtils.getMoneyFormat().format(model.value)
            tvFecha.text = DateUtils.getDateFormat().format(model.date!!)
            tvDescription.text = model.description
            tvInfo.text = root.context.getString(
                R.string.md_info,
                model.personName ?: root.context.getString(R.string.me),
                model.categoryName ?: root.context.getString(R.string.to_define),
            )

            itemView.setOnClickListener { listener?.onMovementClicked(model) }

            if (model.idMovement < 0) {
                ibDiscard.visible()
                ibDiscard.setOnClickListener { listener?.onDiscardMovementClicked(model.idMovement) }
            } else {
                ibDiscard.gone()
            }
        }
    }

    interface OnActionItemListener {
        fun onMovementClicked(movement: MovementModel?)
        fun onDiscardMovementClicked(id: Int)
    }

}

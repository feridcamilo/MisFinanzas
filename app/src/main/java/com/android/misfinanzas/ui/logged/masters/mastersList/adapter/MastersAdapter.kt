package com.android.misfinanzas.ui.logged.masters.mastersList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.RowMasterBinding
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.utils.visible

class MastersAdapter : ListAdapter<MasterModel, MastersAdapter.ViewHolder>(DiffCallback) {

    private var listener: OnActionItemListener? = null

    fun setOnActionItemListener(listener: OnActionItemListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowMasterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<MasterModel>() {
        override fun areItemsTheSame(oldItem: MasterModel, newItem: MasterModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MasterModel, newItem: MasterModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: RowMasterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MasterModel) = with(binding) {
            tvName.text = model.name
            ivLocation.visible(model.latLng != null)
            ivEnable.setImageResource(if (model.enabled) R.drawable.ic_enable else R.drawable.ic_disable)
            itemView.setOnClickListener { listener?.onMasterClicked(model) }
        }
    }

    interface OnActionItemListener {
        fun onMasterClicked(master: MasterModel?)
    }

}

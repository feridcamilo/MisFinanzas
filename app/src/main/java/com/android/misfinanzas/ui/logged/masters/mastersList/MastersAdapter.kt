package com.android.misfinanzas.ui.logged.masters.mastersList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.model.Master
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseViewHolder
import com.android.misfinanzas.base.MasterClickListener
import kotlinx.android.synthetic.main.row_master.view.*

class MastersAdapter(
    private val context: Context,
    private val masters: List<Master>,
    private val itemClickListener: MasterClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MastersViewHolder(LayoutInflater.from(context).inflate(R.layout.row_master, parent, false))
    }

    override fun getItemCount(): Int {
        return masters.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MastersViewHolder -> holder.bind(masters[position], position)
            //put other views here if you want
        }
    }

    //inner class to avoid memory leak, when parent class died (MovementsAdapter) this inner also die
    inner class MastersViewHolder(itemView: View) : BaseViewHolder<Master>(itemView) {
        override fun bind(item: Master, position: Int) {
            itemView.iv_enable.setImageResource(if (item.enabled) R.drawable.ic_enable else R.drawable.ic_disable)
            itemView.tv_name.text = item.name
            itemView.setOnClickListener { itemClickListener.onMasterClicked(item) }
        }
    }
}

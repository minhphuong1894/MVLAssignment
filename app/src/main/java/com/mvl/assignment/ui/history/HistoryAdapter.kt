package com.mvl.assignment.ui.history

import androidx.recyclerview.widget.DiffUtil
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseListAdapter
import com.mvl.assignment.base.BaseViewHolder
import com.mvl.assignment.databinding.ItemHistoryBinding
import com.mvl.assignment.domain.model.HistoryItem
import com.mvl.assignment.domain.model.HistoryList

class HistoryAdapter(
    private val itemClickListener: ((HistoryItem) -> Unit)? = null
) : BaseListAdapter<HistoryItem, ItemHistoryBinding>(object :
    DiffUtil.ItemCallback<HistoryItem>() {
    override fun areItemsTheSame(
        oldItem: HistoryItem,
        newItem: HistoryItem
    ): Boolean {
        return oldItem.price == newItem.price
    }

    override fun areContentsTheSame(
        oldItem: HistoryItem,
        newItem: HistoryItem
    ): Boolean {
        return oldItem == newItem
    }
}) {

    override fun getLayoutRes(viewType: Int): Int {
        return R.layout.item_history
    }

    override fun bindView(
        viewHolder: BaseViewHolder<ItemHistoryBinding>,
        binding: ItemHistoryBinding,
        item: HistoryItem,
        position: Int
    ) {
        super.bindView(viewHolder, binding, item, position)
        with(binding){
            tvLocationNameA.text = item.a?.address ?: ""
            tvLocationLatA.text = (item.a?.lat ?: 0.0).toString()
            tvLocationLngA.text = (item.a?.lng ?: 0.0).toString()
            tvAqiA.text = (item.a?.aqi ?: 0.0).toString()

            tvLocationNameB.text = item.b?.address ?: ""
            tvLocationLatB.text = (item.b?.lat ?: 0.0).toString()
            tvLocationLngB.text = (item.b?.lng ?: 0.0).toString()
            tvAqiB.text = (item.b?.aqi ?: 0.0).toString()
        }
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }
}
package com.mvl.assignment.ui.location

import androidx.recyclerview.widget.DiffUtil
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseListAdapter
import com.mvl.assignment.base.BaseViewHolder
import com.mvl.assignment.databinding.ItemLocationBinding
import com.mvl.assignment.domain.model.LocationTemp
import com.mvl.assignment.util.setSingleClick

class LocationAdapter(
    private val itemClickListener: ((LocationTemp?) -> Unit)? = null
) : BaseListAdapter<LocationTemp, ItemLocationBinding>(object : DiffUtil.ItemCallback<LocationTemp>() {
    override fun areItemsTheSame(oldItem: LocationTemp, newItem: LocationTemp): Boolean {
        return oldItem.lat == newItem.lat
    }

    override fun areContentsTheSame(oldItem: LocationTemp, newItem: LocationTemp): Boolean {
        return oldItem == newItem
    }
}) {

    override fun getLayoutRes(viewType: Int): Int {
        return R.layout.item_location
    }

    override fun bindView(
        viewHolder: BaseViewHolder<ItemLocationBinding>,
        binding: ItemLocationBinding,
        item: LocationTemp,
        position: Int
    ) {
        super.bindView(viewHolder, binding, item, position)
        with(binding){
            this.item = item
        }
        viewHolder.itemView.setSingleClick {
            itemClickListener?.invoke(item)
        }
    }
}
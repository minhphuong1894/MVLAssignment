package com.mvl.assignment.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

private interface BaseAdapter<Item : Any, ViewBinding : ViewDataBinding> {
    /**
     * get layout res based on view type
     */
    fun getLayoutRes(viewType: Int): Int

    /**
     * bind first time
     * use for set item onClickListener, something only set one time
     */
    fun bindFirstTime(binding: ViewBinding) {}

    /**
     * bind view
     */
    fun bindView(viewHolder: BaseViewHolder<ViewBinding>,binding: ViewBinding, item: Item, position: Int) {}

}

/**
 * base recycler view adapter
 */
abstract class BaseListAdapter<Item : Any, ViewBinding : ViewDataBinding>(
    callBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, BaseViewHolder<ViewBinding>>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
), BaseAdapter<Item, ViewBinding> {

    /**
     * override this with new list to pass check "if (newList == mList)" in AsyncListDiffer
     */
    override fun submitList(list: List<Item>?) {
        super.submitList(ArrayList<Item>(list ?: listOf()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            DataBindingUtil.inflate<ViewBinding>(
                LayoutInflater.from(parent.context),
                getLayoutRes(viewType),
                parent, false
            ).apply {
                bindFirstTime(this)
            })
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        val item: Item? = getItem(position)
        holder.binding.setVariable(2, item)
        if (item != null) {
            bindView(holder,holder.binding, item, position)
        }
        holder.binding.executePendingBindings()
    }
}

/**
 * paging 3 adapter
 */
abstract class BasePagingAdapter<Item : Any, ViewBinding : ViewDataBinding>(
    callBack: DiffUtil.ItemCallback<Item>
) : PagingDataAdapter<Item, BaseViewHolder<ViewBinding>>(callBack),
    BaseAdapter<Item, ViewBinding> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            DataBindingUtil.inflate<ViewBinding>(
                LayoutInflater.from(parent.context),
                getLayoutRes(viewType),
                parent, false
            ).apply {
                bindFirstTime(this)
            })
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        // adapter uses getItem() to detect loading
        val item: Item? = getItem(position)
        holder.binding.setVariable(2, item)
        if (item != null) {
            bindView(holder,holder.binding, item, position)
        }
        holder.binding.executePendingBindings()
    }
}

open class BaseViewHolder<ViewBinding : ViewDataBinding>(
    val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root)

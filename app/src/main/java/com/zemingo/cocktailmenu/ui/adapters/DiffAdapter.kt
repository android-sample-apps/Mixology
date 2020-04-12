package com.zemingo.cocktailmenu.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DiffAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun onBindViewHolder(holder: VH, data: DATA, position: Int)

    private val _data = mutableListOf<DATA>()

    fun update(data: List<DATA>) {
        val diffCallback = DiffUtilCallback(_data, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _data.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, _data[position], position)
    }
}

private class DiffUtilCallback<DATA>(
    private val oldList: List<DATA>,
    private val newList: List<DATA>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

}
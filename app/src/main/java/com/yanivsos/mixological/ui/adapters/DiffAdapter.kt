package com.yanivsos.mixological.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yanivsos.mixological.ui.utils.InputActions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

abstract class DiffAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun onBindViewHolder(holder: VH, data: DATA, position: Int)

    private val _data = mutableListOf<DATA>()

    private val inputActionsChannel = ConflatedBroadcastChannel<InputActions<DATA>>()
    val inputActions: Flow<InputActions<DATA>> = inputActionsChannel.asFlow()

    protected fun sendInputAction(inputActions: InputActions<DATA>) {
        GlobalScope.launch {
            inputActionsChannel.send(inputActions)
        }
    }

    protected fun getData(position: Int) = _data[position]

    protected open fun calculateDiff(old: List<DATA>, new: List<DATA>): DiffUtil.DiffResult {
        val diffCallback = DiffUtilCallback(old, new)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun update(data: List<DATA>) {
        val diffResult = calculateDiff(_data, data)
        _data.run {
            clear()
            addAll(data)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    fun clear() {
        update(emptyList())
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

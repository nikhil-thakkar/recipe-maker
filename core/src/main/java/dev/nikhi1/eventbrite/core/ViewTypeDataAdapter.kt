package dev.nikhi1.eventbrite.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
/**
 *  Adapter to populate the list of list
 */
class ViewTypeAdapter<E : ViewType<*>>(
    private var list: MutableList<E> = mutableListOf(),
    private val onItemActionListener: OnItemActionListener? = null
) : RecyclerView.Adapter<ViewTypeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTypeHolder {

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
        return ViewTypeHolder(binding, onItemActionListener)
    }

    override fun onBindViewHolder(holder: ViewTypeHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun getItemViewType(position: Int): Int = list[position].layoutId()

    override fun getItemCount(): Int = list.size

    fun setList(list: List<E>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun insertElement(data: E, position: Int? = null) {
        if (position != null) {
            this.list.add(position, data)
            notifyItemInserted(position)
        } else {
            this.list.add(data)
            notifyItemInserted(this.list.size - 1)
        }
    }

    fun updateElement(data: E, position: Int) {
        this.list[position] = data
        notifyItemChanged(position)
    }
}

class ViewTypeHolder(private val binding: ViewDataBinding, private val onItemActionListener: OnItemActionListener?) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: ViewType<*>) {
        binding.setVariable(BR.model, item.data())
        if (item.isUserInteractionEnabled()) {
            binding.setVariable(BR.position, adapterPosition)
            binding.setVariable(BR.actionItemListener, onItemActionListener)
        }
        binding.executePendingBindings()
    }
}

interface OnItemActionListener {
    fun onItemClicked(position: Int)
}
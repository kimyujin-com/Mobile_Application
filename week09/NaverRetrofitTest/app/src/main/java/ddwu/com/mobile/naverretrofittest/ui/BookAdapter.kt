package ddwu.com.mobile.naverretrofittest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.naverretrofittest.data.Item
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookHolder>() {
    var books: List<Item>? = null

    override fun getItemCount(): Int {
        return books?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.itemBinding.tvItem.text = books?.get(position).toString()
        holder.itemBinding.clItem.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class BookHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: OnItemClickListner) {
        this.clickListener = listener
    }

}
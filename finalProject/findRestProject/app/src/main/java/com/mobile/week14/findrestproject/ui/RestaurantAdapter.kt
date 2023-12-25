package com.mobile.week14.findrestproject.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.mobile.week14.findrestproject.data.Item
import com.mobile.week14.findrestproject.data.RestaurantDatabase
import com.mobile.week14.findrestproject.databinding.SearchlistItemBinding

class RestaurantAdapter(private val context: Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder>(){
    var restaurants: List<Item>? = null
    val restaurantDB = RestaurantDatabase.getInstance(context)!!

    // 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    private lateinit var mItemClickListener: OnItemClickListener

    fun setMyItemClickListener(itemClickListener: OnItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "갯수:"+ restaurants?.size)
        return restaurants?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {
        var itemBinding = SearchlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        holder.itemBinding.itemRestaurantTitleTv.text = restaurants?.get(position)?.title
        holder.itemBinding.itemRestaurantCategoryTv.text = restaurants?.get(position)?.category
        holder.itemBinding.itemRestaurantAddressTv.text = restaurants?.get(position)?.address
        holder.itemBinding.clItem.setOnClickListener{
            mItemClickListener?.onItemClick(it, position)
        }
    }

    class RestaurantHolder(val itemBinding: SearchlistItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

}
package com.mobile.week14.findrestproject.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.week14.findrestproject.R
import com.mobile.week14.findrestproject.data.Item
import com.mobile.week14.findrestproject.data.RestaurantDatabase
import com.mobile.week14.findrestproject.data.myRestaurant
import com.mobile.week14.findrestproject.databinding.FoodlistItemBinding

class FoodListAdapter(private val context: Context): RecyclerView.Adapter<FoodListAdapter.FoodListHolder>(){
    var foodList: List<myRestaurant>? = null

    // 인터페이스 정의
    interface OnItemLongClickListener {
        fun  onItemLongClick(view: View, position: Int)
    }

    private lateinit var mItemLongClickListener: OnItemLongClickListener

    fun setOnItemLongClickListener(itemClickListener: OnItemLongClickListener){
        mItemLongClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private lateinit var mItemClickListener: OnItemClickListener

    fun setOnItemClickListener(itemClickListener: OnItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return foodList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListHolder {
        val itemBinding = FoodlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodListHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FoodListHolder, position: Int) {
        holder.itemBinding.itemRestauranctTitleTv.text = foodList?.get(position)?.name
        holder.itemBinding.itemRestauranctCategoryTv.text = foodList?.get(position)?.category
        holder.itemBinding.itemRestauranctAddressTv.text = foodList?.get(position)?.address
        holder.itemBinding.memo.text = foodList?.get(position)?.memo
        val imagePath = foodList?.get(position)?.photoPath
        val imageBitmap = decodeSampledBitmapFromFile(imagePath, 100, 100) // Adjust width and height as needed
        holder.itemBinding.imageView.setImageBitmap(imageBitmap)
        holder.itemBinding.shareButton.setOnClickListener {
            mItemClickListener?.onItemClick(it, position)
            true
        }
        holder.itemBinding.itemRestaurantHeartIv.setOnLongClickListener {
            mItemLongClickListener?.onItemLongClick(it, position)
            true
        }
    }

    class FoodListHolder(val itemBinding: FoodlistItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private fun decodeSampledBitmapFromFile(path: String?, reqWidth: Int, reqHeight: Int): Bitmap {
        if (path == null || path.isEmpty()) {
            return getDefaultBitmap()
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getDefaultBitmap(): Bitmap {
        val placeholderResId = R.drawable.ic_launcher_background
        return try {
            BitmapFactory.decodeResource(context.resources, placeholderResId)
        } catch (e: Exception) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }
}
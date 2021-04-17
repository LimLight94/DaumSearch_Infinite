package com.moong.programers.main.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.moong.programers.databinding.ItemListBinding
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.DiffUtil
import com.moong.programers.data.ItemData
import com.moong.programers.utils.ShowDialogEvent
import org.greenrobot.eventbus.EventBus
import kotlin.collections.ArrayList

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val mItems: ArrayList<ItemData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener {
            binding.bean?.docUrl?.let {
                EventBus.getDefault().post(ShowDialogEvent(it))
            }
        }
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mItems[position])
        setFadeAnimation(holder.itemView)
    }

    //    fun setItems(list: List<ItemData>){
//        mItems.clear()
//        mItems.addAll(list)
//        notifyDataSetChanged()
//    }
    fun setItems(list: List<ItemData>) {
        val diffCallback = ItemDiffCallback(mItems, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mItems.clear()
        mItems.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

//    fun addItems(list: List<ItemData>) {
//        val a = mItems.size
//        val b = list.size
//        mItems.addAll(list.subList(a, b))
////        mItems.addAll(list)
//        notifyItemRangeChanged(mItems.size, list.size)
//    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 400
        view.startAnimation(anim)
    }

    class ItemViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemData) {
            binding.bean = item
        }
    }

    class ItemOffsetDecoration(var mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
        }
    }
}
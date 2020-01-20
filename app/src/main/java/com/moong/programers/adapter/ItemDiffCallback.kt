package com.moong.programers.adapter

import androidx.recyclerview.widget.DiffUtil
import com.moong.programers.data.ItemData

/**
 * ChallangeProject
 * Class: ItemDiffCallback
 * Created by appg on 2020-01-20.
 *
 * Description:
 */
class ItemDiffCallback(
        private val oldList: List<ItemData>,
        private val newList: List<ItemData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].title == newList[newItemPosition].title

}
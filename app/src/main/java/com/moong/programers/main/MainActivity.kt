package com.moong.programers.main

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.moong.programers.R
import com.moong.programers.base.BaseActivity
import com.moong.programers.data.ItemData
import com.moong.programers.databinding.MainActivityBinding;

class MainActivity : BaseActivity<MainActivityBinding>() {

    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mViewModel = getViewModel(MainViewModel::class.java)
        mBinding.viewModel = mViewModel

        init()
    }
    private fun init(){
        initRecyclerView(mBinding.list, ItemAdapter())


    }
    private fun initRecyclerView(recyclerView : RecyclerView, adapter: ItemAdapter){
        try {
            val layoutManager = GridLayoutManager(recyclerView.context,2)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.itemAnimator
            recyclerView.addItemDecoration(ItemAdapter.ItemOffsetDecoration(recyclerView.context,R.dimen.item_offset))
            recyclerView.addOnScrollListener(object :EndlessRecyclerViewScrollListener(layoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    mViewModel.loadNextPage()
                }

            })
        } catch (e: Exception) {
            throw IllegalArgumentException("Target class not inherit ViewTypeRecyclerAdapter", e)
        }
    }
}

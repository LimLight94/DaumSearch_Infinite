package com.moong.programers.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moong.programers.R
import com.moong.programers.base.BaseActivity
import com.moong.programers.constants.Constants
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
        mBinding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0-> mViewModel.mSkinType.set(Constants.API_SKIN_TYPE_OILY)
                    1-> mViewModel.mSkinType.set(Constants.API_SKIN_TYPE_OILY)
                    2-> mViewModel.mSkinType.set(Constants.API_SKIN_TYPE_DRY)
                    3-> mViewModel.mSkinType.set(Constants.API_SKIN_TYPE_SENSITIVE)
                }
            }

        }
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

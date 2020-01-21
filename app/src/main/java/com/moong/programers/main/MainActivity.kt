package com.moong.programers.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moong.programers.R
import com.moong.programers.adapter.EndlessRecyclerViewScrollListener
import com.moong.programers.adapter.ItemAdapter
import com.moong.programers.base.BaseActivity
import com.moong.programers.databinding.MainActivityBinding;
import com.moong.programers.utils.DoubleBackInvoker
import com.moong.programers.utils.ShowDialogEvent
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity<MainActivityBinding>() {

    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mViewModel = getViewModel(MainViewModel::class.java)
        mBinding.viewModel = mViewModel

        init()
    }

    private fun init() {
        mBinding.layout.isTouchEnabled = false
        initRecyclerView(mBinding.list, ItemAdapter())
        mBinding.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mViewModel.mKeyWord.set(mBinding.editText.text.toString())
            }
            false
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, itemAdapter: ItemAdapter) = try {
        val gridlayoutManager = GridLayoutManager(recyclerView.context, 2)
        recyclerView.apply {
            layoutManager = gridlayoutManager
            adapter = itemAdapter
            addItemDecoration(ItemAdapter.ItemOffsetDecoration(context, R.dimen.item_offset))
            addOnScrollListener(object : EndlessRecyclerViewScrollListener(gridlayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    mViewModel.loadNextPage()
                }
            })
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onBackPressed() {
        if (mBinding.layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mBinding.layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        } else {
            DoubleBackInvoker.execute(R.string.app_close_message)
        }

    }

    @Subscribe
    fun getShowDialogEvent(event: ShowDialogEvent) {
        if (event.itemId == -1) {
            mBinding.layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        } else {
            mViewModel.getItemDetail(event.itemId)
            mBinding.layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }
}

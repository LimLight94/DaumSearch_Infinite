package com.moong.programers.detail

import android.os.Bundle
import com.moong.programers.R
import com.moong.programers.base.BaseActivity
import com.moong.programers.databinding.DetailActivityBinding

class DetailActivity : BaseActivity<DetailActivityBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val mViewModel = getViewModel(DetailViewModel::class.java)
        mBinding.viewModel = mViewModel
    }
}

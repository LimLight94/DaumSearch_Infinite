package com.moong.programers.detail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moong.programers.R
import com.moong.programers.base.BaseViewModel
import com.moong.programers.base.impl.BaseInterface
import com.moong.programers.databinding.DetailDialogBinding

/**
 * ChallangeProject
 * Class: DetailDialog
 * Created by appg on 2020-01-09.
 *
 * Description:
 */
class DetailDialog : BottomSheetDialogFragment() , BaseInterface {
    private var viewModelProvideFactory: ViewModelProvider.Factory? = null
    private lateinit var mDialogViewModel: DetailDialogViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DetailDialogBinding.inflate(inflater,container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialogViewModel = getViewModel(DetailDialogViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return super.show(transaction, tag)
    }

    private fun <T : BaseViewModel> getViewModel(viewModelClass: Class<T>): T {
        if(viewModelProvideFactory==null)
            viewModelProvideFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        val viewModel = getViewModel(this, viewModelProvideFactory, viewModelClass)

        setViewModelObject(viewModel)
        return viewModel
    }
    private fun setViewModelObject(viewModel: BaseViewModel) {
        lifecycle.addObserver(viewModel)
        viewModel.lifecycle = lifecycle
    }
}
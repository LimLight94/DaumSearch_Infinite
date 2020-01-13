package com.moong.programers.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moong.programers.base.BaseViewModel
import com.moong.programers.base.impl.BaseInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.moong.programers.databinding.DetailDialogBinding
import pyxis.uzuki.live.richutilskt.utils.windowManager
import android.util.DisplayMetrics




/**
 * ChallangeProject
 * Class: DetailDialog
 * Created by appg on 2020-01-09.
 *
 * Description:
 */
class DetailDialog(val itemId : Int) : BottomSheetDialogFragment(), BaseInterface {
    private var viewModelProvideFactory: ViewModelProvider.Factory? = null
    private lateinit var mDialogViewModel: DetailDialogViewModel
    private lateinit var mBinding: DetailDialogBinding


    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
            if(slideOffset==0f){
                dismiss()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DetailDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialogViewModel = getViewModel(DetailDialogViewModel::class.java)
        mBinding.viewModel = mDialogViewModel
//        view.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
//            override fun onGlobalLayout() {
//                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
////                if (Build.VERSION.SDK_INT < 16) {
////                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
////                } else {
////                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
////                }
//                val dialog = dialog as BottomSheetDialog?
//                dialog!!.setCanceledOnTouchOutside(false)
//                val bottomSheet =
//                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
////                bottomSheet!!.minimumHeight = 800
//                val displayMetrics = DisplayMetrics()
//                context?.windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
//                val realDeviceHeight = displayMetrics.heightPixels
////                bottomSheet!!.minimumHeight = realDeviceHeight* 0.9.toInt()
//                bottomSheet!!.setBackgroundColor(view.context.getColor(com.moong.programers.R.color.transParent))
//                val behavior = BottomSheetBehavior.from(bottomSheet)
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                behavior.peekHeight = realDeviceHeight*0.7.toInt()
////                behavior.peekHeight = 0 // Remove this line to hide a dark background if you manually hide the dialog.
//                behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
//            }
//        })
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        mDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//        mDialog.setOnShowListener {
//            val d = it as BottomSheetDialog
//            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            behavior = BottomSheetBehavior.from(sheet)
//            behavior.isHideable = false
//            behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
//        }
//        return mDialog
//    }

    private fun <T : BaseViewModel> getViewModel(viewModelClass: Class<T>): T {
        if (viewModelProvideFactory == null)
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
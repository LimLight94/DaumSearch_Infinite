package com.moong.programers.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity(), BaseInterface {
    private var viewModelProvideFactory: ViewModelProvider.Factory? = null

    protected lateinit var mBinding: V
    protected val compositeDisposable = CompositeDisposable()

    private var viewModel: BaseViewModel? by weak(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerEventBus()
    }

    override fun setContentView(layoutResID: Int) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, null, false)
        super.setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        registerEventBus()
    }

    override fun onStop() {
        super.onStop()
        unregisterEventBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        viewModel?.let { lifecycle.removeObserver(it) }
        unregisterEventBus()
    }

    fun <T : BaseViewModel> getViewModel(viewModelClass: Class<T>): T {
        if(viewModelProvideFactory==null)
            viewModelProvideFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = getViewModel(this, viewModelProvideFactory, viewModelClass)
        setViewModelObject(viewModel)
        return viewModel
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


    @Deprecated("Don't need to call manually")
    protected fun setViewModelReference(viewModel: BaseViewModel) {
        setViewModelObject(viewModel)
    }

    private fun setViewModelObject(viewModel: BaseViewModel) {
        lifecycle.addObserver(viewModel)
        viewModel.lifecycle = lifecycle
        this.viewModel = viewModel
    }

    private fun registerEventBus() {
        try {
            EventBus.getDefault().register(this)
        } catch (t: Throwable) {
            Log.e("exception","$t:" + t.message)
        }
    }

    private fun unregisterEventBus() {
        try {
            EventBus.getDefault().unregister(this)
        } catch (t: Throwable) {
            Log.e("exception","$t:" + t.message)
        }

    }
}
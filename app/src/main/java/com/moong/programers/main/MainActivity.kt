package com.moong.programers.main

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moong.programers.R
import com.moong.programers.base.BaseActivity
import com.moong.programers.databinding.MainActivityBinding
import com.moong.programers.main.adapter.EndlessRecyclerViewScrollListener
import com.moong.programers.main.adapter.ItemAdapter
import com.moong.programers.utils.DoubleBackInvoker
import com.moong.programers.utils.EnhanceWebView
import com.moong.programers.utils.ShowDialogEvent
import com.moong.programers.utils.UpBtnEvent
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.main_activity.view.*
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity<MainActivityBinding>(), EnhanceWebView.EnhanceWebViewHandler {

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


        mBinding.webView.webViewHandler = this
        mBinding.webView.additionalUserAgent = "_base"
        mBinding.webView.enableFormUpload(this,"image/*")
        mBinding.webView.setUrl("")

        // 쿠키 허용
        val cookieManager = CookieManager.getInstance()

        if (Build.VERSION.SDK_INT >= 21) {
            val web: WebSettings = mBinding.webView.webView.getSettings()
            web.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cookieManager.setAcceptThirdPartyCookies(mBinding.webView.webView, true)
        }

        cookieManager.setAcceptCookie(true)

    }

    private fun initRecyclerView(recyclerView: RecyclerView, itemAdapter: ItemAdapter) = try {
        val gridlayoutManager = GridLayoutManager(recyclerView.context, 2)

        recyclerView.apply {
            layoutManager = gridlayoutManager
            adapter = itemAdapter
            addItemDecoration(ItemAdapter.ItemOffsetDecoration(context, R.dimen.item_offset))
            addOnScrollListener(object : EndlessRecyclerViewScrollListener(gridlayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
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
        if (event.itemUrl.equals("close")) {
            mBinding.layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        } else {
            if(!event.itemUrl.equals(mBinding.webView.mUrl)) {
                mViewModel.mIsDetail_Loading.set(true)
                mBinding.webView.setUrl(event.itemUrl)
            }

            mBinding.layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }

    @Subscribe
    fun getUpBtnEvent(event: UpBtnEvent) {
        if (event.isTrue) {
            mBinding.list.scrollToPosition(0)
            mBinding.header.setExpanded(true)
        }
    }

    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String,
        uri: Uri,
        scheme: String?,
        host: String?,
        parameters: Map<String, String>?
    ): Boolean {
        if (url.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
            startActivity(intent)
            return true
        } else if (url.startsWith("mailto:")) {
            val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
            startActivity(i)
            return true
        }

        return false

    }

    override fun onPageFinished(view: WebView, url: String) {
        CookieManager.getInstance().flush()
        mViewModel.mIsDetail_Loading.set(false)
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        mViewModel.mIsDetail_Loading.set(true)
    }
}

package com.moong.programers.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import android.widget.FrameLayout
import com.google.common.base.Splitter
import com.moong.programers.base.impl.ActivityInterface
import com.moong.programers.base.impl.AlertInterface
import com.moong.programers.base.weak
import pyxis.uzuki.live.richutilskt.utils.createUri
import java.util.HashMap

/**
 * ChallangeProjects
 * Class: EnhanceWebView
 * Created by limmoong on 2021/04/17.
 *
 * Description:
 */
open class EnhanceWebView : FrameLayout, ActivityInterface, AlertInterface {
    var mUrl: String = ""
    var mAssertUrl: String = ""

    var onShouldInterceptRequestListener: OnShouldInterceptRequestListener? = null
    var webViewHandler: EnhanceWebViewHandler? = null
    var onReceivedErrorListener: OnReceivedErrorListener? = null
    var onBackButtonListener: OnBackButtonListener? = null
    var enableGoBack: Boolean = false
    var additionalUserAgent = ""

    private lateinit var mWebView: WebView
    private var uploadMessage: ValueCallback<Uri>? = null
    private var uploadMessages: ValueCallback<Array<Uri>>? = null
    private var activity: Activity? by weak(null)
    private var mContentMimeType = "*/*"
    private lateinit var mCapturedImageURI: Uri
    private var enableCamera: Boolean = false

    /**
     * get [WebView] object which handling now
     *
     * @return [WebView]
     */
    val webView: WebView
        get() = getChildAt(0) as WebView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    /**
     * set URL to load
     *
     * @param url String
     */
    fun setUrl(url: String) {
        mUrl = url
        loadWebView()
    }


    private fun init() {
        mWebView = WebView(context)
        addView(mWebView)

        if (!TextUtils.isEmpty(mUrl) || !TextUtils.isEmpty(mAssertUrl)) {
            loadWebView()
        }
    }

    private fun showFileChooserKitkat() {
        if (activity == null) return

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = mContentMimeType
        activity?.startActivityForResult(
            Intent.createChooser(intent, "Choose File"),
            FORM_REQUEST_CODE
        )
    }

    private fun showFileChooser() {
        if (activity == null) return

        if (!enableCamera) {
            showFileChooserKitkat()
            return
        }

        mCapturedImageURI = createUri(context, false, false)

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = mContentMimeType

        val chooserIntent = Intent.createChooser(intent, "Choose File")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))

        activity?.startActivityForResult(chooserIntent, FORM_REQUEST_CODE)
    }

    /**
     * loading webview
     *
     * @param webView [WebView]
     */
    @JvmOverloads
    fun loadWebView(webView: WebView? = mWebView) {
        if (TextUtils.isEmpty(mUrl) && !TextUtils.isEmpty(mAssertUrl)) {
            mUrl = String.format("file:///android_asset/%s", mAssertUrl)
        }

        loadWebView(webView ?: mWebView, mUrl)
    }

    @JvmOverloads
    fun enableFormUpload(
        activity: Activity,
        contentMimeType: String = "*/*",
        enableCamera: Boolean = false
    ) {
        this.activity = activity
        mContentMimeType = contentMimeType
        this.enableCamera = enableCamera
    }

    /**
     * loading webview with Url
     *
     * @param webView [WebView]
     * @param url     String
     */
    fun loadWebView(webView: WebView, url: String) {
        setDefaultWebSettings(webView)
        webView.loadUrl(url)
    }

    private fun setDefaultWebSettings(webView: WebView) {
        val webSettings = webView.settings
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true)

        if (!TextUtils.isEmpty(additionalUserAgent)) {
            webSettings.userAgentString =
                String.format("%s %s", webSettings.userAgentString, additionalUserAgent)
        }

        webView.webViewClient = WebClient()
        webView.webChromeClient = ChromeClient()
    }

    private fun getParameters(uri: String): Map<String, String>? {
        try {
            var map: Map<String, String> = HashMap()
            if (!uri.contains("?")) return map
            val query = uri.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            map = Splitter.on('&').trimResults().omitEmptyStrings().withKeyValueSeparator("=")
                .split(query)
            return map
        } catch (e: Exception) {
            // sometime we can't catch parameters cause url string is not valid (ex, pay within Samsung Pay)
            return null
        }
    }


    private inner class WebClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            AlertDialog.Builder(context)
                .setMessage("There is a problem with the security certificate. Do you want to continue?")
                .setPositiveButton("Proceed") { _, _ -> handler.proceed() }
                .setNegativeButton("Cancel") { _, _ -> handler.cancel() }
                .show()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d(TAG, String.format("url: %s", url))
            val uri = Uri.parse(url)

            if (webViewHandler != null && webViewHandler?.shouldOverrideUrlLoading(
                    view, url, uri, uri.scheme,
                    uri.host, getParameters(url)
                ) == true
            ) {
                return true
            }

            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            webViewHandler?.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            webViewHandler?.onPageFinished(view, url)
        }

        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            return onShouldInterceptRequestListener?.shouldInterceptRequest(view, url)
                ?: super.shouldInterceptRequest(view, url)
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(
                    TAG, "onReceivedError: request= "
                            + request.url + "error: " + error.errorCode + "desc:" + error.description
                )
            }

            onReceivedErrorListener?.onReceiveError(false, request, error)
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.d(
                TAG, "onReceivedError: request= "
                        + failingUrl + "error: " + errorCode + "desc:" + description
            )
            onReceivedErrorListener?.onReceiveError(false, null, null)
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            onReceivedErrorListener?.onReceiveError(true, request, errorResponse)
        }
    }

    private inner class ChromeClient : WebChromeClient() {

        override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
            return true
        }

        override fun onCreateWindow(
            view: WebView,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message
        ): Boolean {
            val newWebView = WebView(view.context)
            loadWebView(newWebView)
            addView(newWebView)

            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = newWebView
            resultMsg.sendToTarget()
            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            removeView(window)
        }

        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: android.webkit.JsResult
        ): Boolean {
            showAlertDialog(message) { _, _ -> result.confirm() }
            return true
        }

        override fun onJsConfirm(
            view: WebView,
            url: String,
            message: String,
            result: android.webkit.JsResult
        ): Boolean {
            showConfirmDialog(message, { _, _ -> result.confirm() }, { _, _ -> result.cancel() })
            return true
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
            uploadMessage = uploadMsg
            showFileChooserKitkat()
        }

        @Suppress("UNCHECKED_CAST")
        fun openFileChooser(uploadMsg: ValueCallback<*>, acceptType: String) {
            uploadMessage = uploadMsg as ValueCallback<Uri>
            if (acceptType.isNotEmpty()) {
                mContentMimeType = acceptType
            }
            showFileChooserKitkat()
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            uploadMessage = uploadMsg
            if (acceptType.isNotEmpty()) {
                mContentMimeType = acceptType
            }
            showFileChooserKitkat()
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: WebChromeClient.FileChooserParams
        ): Boolean {
            uploadMessages = filePathCallback
            if (fileChooserParams.acceptTypes.joinToString(",").isNotEmpty()) {
                mContentMimeType = fileChooserParams.acceptTypes.joinToString(",")
            }
            showFileChooser()
            return true
        }
    }

    interface EnhanceWebViewHandler {
        fun shouldOverrideUrlLoading(
            view: WebView, url: String, uri: Uri, scheme: String?, host: String?,
            parameters: Map<String, String>?
        ): Boolean

        fun onPageFinished(view: WebView, url: String)

        fun onPageStarted(view: WebView, url: String, favicon: Bitmap?)
    }

    interface OnShouldInterceptRequestListener {
        fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse
    }

    interface OnBackButtonListener {
        fun onBackPressed(url: String): Boolean
    }

    interface OnReceivedErrorListener {
        fun onReceiveError(
            receiveHttpError: Boolean,
            resourceRequest: WebResourceRequest?,
            `object`: Any?
        )
    }

    companion object {
        const val FORM_REQUEST_CODE = 72
        const val TAG = "EnhanceWebView"
    }

}
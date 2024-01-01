package com.khantnyar.web_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
//import com.khantnyar.pornx.databinding.ActivityMainBinding
import com.khantnyar.web_view.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var TAG = "MainActivity"
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // For handling file input
    private var fileUploadCallback: ValueCallback<Array<android.net.Uri>>? = null
    private val FILE_CHOOSER_RESULT_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)//setContentView(R.layout.activity_main)

        binding.webView.loadUrl("https://www.google.com/")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportMultipleWindows(true)

        binding.webView.webViewClient = WebViewClient()//set the webview clients
        binding.webView.webChromeClient = MyWebChromeClient()//get Custom Client For WebView
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()){
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // Custom Client For WebView
    inner class MyWebChromeClient : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<android.net.Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            fileUploadCallback = filePathCallback
            val intent = fileChooserParams?.createIntent()
            if (intent != null) {
                startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE)
            }
            return true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (fileUploadCallback != null) {
                if (resultCode == android.app.Activity.RESULT_OK) {
                    fileUploadCallback!!.onReceiveValue(
                        arrayOf(data?.data!!)
                    )
                } else {
                    fileUploadCallback!!.onReceiveValue(null)
                }
                fileUploadCallback = null
            }
        }
    }
}
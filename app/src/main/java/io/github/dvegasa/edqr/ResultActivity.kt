package io.github.dvegasa.edqr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private var qrRawResult: Result? = null

    private var qrText: String? = null
    private var qrType: String? = null

    private var actionHandler: ActionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        loadResult()
        showResult()
    }

    private fun loadResult() {
        qrRawResult = InMemoryStorage.qrResult
        if (qrRawResult == null) {
            showLoadingError()
            finish()
        } else {
            qrText = qrRawResult?.text
            qrType = ResultParser.parseResult(qrRawResult).type.toString()
            actionHandler = ActionHandler(qrRawResult!!)
        }
    }

    private fun showLoadingError() {
        Toast.makeText(this, "Scanning error", Toast.LENGTH_LONG).show()
        Exception("Scanning error").printStackTrace()
    }

    private fun showResult() {
        initTvBody(qrText ?: "")
        initTypeText(qrType ?: "")
        initActionButton()
        initCopyButton()
        initShareButton()
    }

    private fun initTypeText(qrType: String) {
        if (typeToString(qrType).isEmpty()) {
            tvType.visibility = View.GONE
            return
        }
        tvType.visibility = View.VISIBLE

        val text = "Recognized as "
        val spannableString = SpannableString(text + typeToString(qrType))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), text.length, spannableString.length, 0)
        tvType.setText(spannableString)
    }

    private fun initTvBody(qrText: String) {
        tvQrBody.setText(qrText)
    }

    private fun initActionButton() {
        if (typeToString(qrType ?: "").isEmpty()) {
            btnAction.visibility = View.INVISIBLE
        } else {
            btnAction.visibility = View.VISIBLE
        }

        btnAction.setText(typeToActionButtonText(qrType ?: ""))
        btnAction.setOnClickListener {
            actionHandler?.doAction()
        }
    }


    private fun initCopyButton() {
        btnCopy.setOnClickListener {
            val clipboard: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("qrText", qrText)
            clipboard!!.setPrimaryClip(clip)

            btnCopy.isEnabled = false
            btnCopy.setText("COPIED")
            Thread(Runnable {
                Thread.sleep(1000L)
                btnCopy.post {
                    btnCopy.isEnabled = true
                    btnCopy.setText("COPY")
                }
            }).start()
        }
    }

    private fun initShareButton() {
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, qrText)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    private fun typeToString(qrType: String) =
        when (qrType) {
            "ADDRESSBOOK" -> "business card"
            "EMAIL_ADDRESS" -> "e-mail"
            "PRODUCT" -> ""
            "URI" -> "web URL"
            "TEXT" -> ""
            "GEO" -> "geo point"
            "TEL" -> "telephone"
            "SMS" -> "SMS"
            "CALENDAR" -> "calendar event"
            "WIFI" -> "Wi-Fi connection"
            "ISBN" -> ""
            "VIN" -> ""
            else -> ""
        }

    private fun typeToActionButtonText(qrType: String) =
        when (qrType) {
            "ADDRESSBOOK" -> "ADD"
            "EMAIL_ADDRESS" -> "WRITE"
            "PRODUCT" -> ""
            "URI" -> "OPEN"
            "TEXT" -> ""
            "GEO" -> "OPEN MAP"
            "TEL" -> "ADD"
            "SMS" -> "SEND"
            "CALENDAR" -> "ADD"
            "WIFI" -> "CONNECT"
            "ISBN" -> ""
            "VIN" -> ""
            else -> ""
        }

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, ResultActivity::class.java)
        }
    }
}

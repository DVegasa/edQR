package io.github.dvegasa.edqr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser
import kotlinx.android.synthetic.main.activity_result.*

private const val ARG_QR_TEXT = "qrText"
private const val ARG_QR_TYPE = "qrType"

class ResultActivity : AppCompatActivity() {

    private var qrText: String? = null
    private var qrType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        qrText = intent.extras?.getString(ARG_QR_TEXT)
        qrType = intent.extras?.getString(ARG_QR_TYPE)

        Log.d("ed__", "$qrText;$qrType")

        initTvBody(qrText ?: "")
        initTypeText(tvType, qrType ?: "")
        initActionButton()
        initCopyButton()
        initShareButton()
    }

    private fun initTvBody(qrText: String) {
        tvQrBody.setText(qrText)
    }

    private fun initTypeText(tvType: TextView, qrType: String) {
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

    private fun initActionButton() {
        if (typeToString(qrType ?: "").isEmpty()) {
            btnAction.visibility = View.INVISIBLE
        } else {
            btnAction.visibility = View.VISIBLE
        }

        btnAction.setText(typeToActionButtonText(qrType ?: ""))
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
        fun getIntent(context: Context, result: Result): Intent {
            val parsed = ResultParser.parseResult(result)

            Log.d("ed__", "${result.text};${parsed.type}")

            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(ARG_QR_TEXT, result.text)
            intent.putExtra(ARG_QR_TYPE, parsed.type.name)
            return intent
        }
    }
}

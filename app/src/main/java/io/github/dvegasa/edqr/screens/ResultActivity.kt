package io.github.dvegasa.edqr.screens

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
import io.github.dvegasa.edqr.ActionHandler
import io.github.dvegasa.edqr.InMemoryStorage
import io.github.dvegasa.edqr.R
import io.github.dvegasa.edqr.ResultTranscriptor
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private var qrTranscriptor: ResultTranscriptor? = null
    private var actionHandler: ActionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        loadResult()
        showResult()
        initCopyButton()
        initShareButton()
    }

    private fun loadResult() {
        val qrRawResult = InMemoryStorage.qrResult
        if (qrRawResult == null) {
            showLoadingError()
            finish()
        } else {
            qrTranscriptor = ResultTranscriptor(qrRawResult)
            actionHandler = ActionHandler(this, qrRawResult)
        }
    }

    private fun showLoadingError() {
        Toast.makeText(this, "Scanning error", Toast.LENGTH_LONG).show()
        Exception("Scanning error").printStackTrace()
    }

    private fun showResult() {
        initTvBody(shownText = qrTranscriptor?.getText() ?: "")
        initTypeText(typeHint = qrTranscriptor?.getTypeHint() ?: "")
        initActionButton(actionHint = qrTranscriptor?.getActionHint() ?: "")
    }

    private fun initTypeText(typeHint: String) {
        if (typeHint.isEmpty()) {
            tvType.visibility = View.GONE
            return
        }
        tvType.visibility = View.VISIBLE

        val text = "Recognized as "
        val spannableString = SpannableString(text + typeHint)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), text.length, spannableString.length, 0)
        tvType.setText(spannableString)
    }

    private fun initTvBody(shownText: String) {
        tvQrBody.setText(shownText)
    }

    private fun initActionButton(actionHint: String) {
        if (actionHint.isEmpty()) {
            btnAction.visibility = View.INVISIBLE
        } else {
            btnAction.visibility = View.VISIBLE
        }

        btnAction.setText(actionHint)
        btnAction.setOnClickListener {
            actionHandler?.doAction()
        }
    }


    private fun initCopyButton() {
        btnCopy.setOnClickListener {
            val clipboard: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("qrText", qrTranscriptor?.getText())
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, qrTranscriptor?.getText())
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, ResultActivity::class.java)
        }
    }
}

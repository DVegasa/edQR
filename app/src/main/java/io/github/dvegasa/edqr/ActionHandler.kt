package io.github.dvegasa.edqr

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.zxing.Result

/**
 * 12.11.2019
 */
class ActionHandler(val context: Context, qrRawResult: Result) {

    private val qrTranscriptor = ResultTranscriptor(qrRawResult)

    fun doAction() {
        when (qrTranscriptor.getType()) {
            QrType.BUSINESS_CARD -> addAddressBook()
            QrType.EMAIL -> emailTo()
            QrType.TEXT -> null
            QrType.URI -> openUri()
            QrType.GEO -> openMap()
            QrType.TEL -> addTel()
            QrType.SMS -> sendSms()
            QrType.CALENDAR_EVENT -> addToCalendar()
            QrType.WIFI -> connectWifi()
        }
    }

    private fun addAddressBook() {

    }

    private fun emailTo() {

    }

    private fun openUri() {
        var text = qrTranscriptor.getText()
        if (!text.startsWith("http", ignoreCase = true)) {
            text = "http://$text"
        }
        val uri = Uri.parse(text)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(context, intent, null)
    }

    private fun openMap() {

    }

    private fun addTel() {

    }

    private fun sendSms() {

    }

    private fun addToCalendar() {

    }

    private fun connectWifi() {

    }
}

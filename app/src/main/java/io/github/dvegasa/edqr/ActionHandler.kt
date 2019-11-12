package io.github.dvegasa.edqr

import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser

/**
 * 12.11.2019
 */
class ActionHandler(private val qrRawResult: Result) {
    private val parsedResult = ResultParser.parseResult(qrRawResult)

    fun doAction() {
        when (parsedResult.type.toString()) {
            "ADDRESSBOOK" -> addAddressBook()
            "EMAIL_ADDRESS" -> emailTo()
            "PRODUCT" -> null
            "URI" -> openUri()
            "TEXT" -> null
            "GEO" -> openMap()
            "TEL" -> addTel()
            "SMS" -> sendSms()
            "CALENDAR" -> addToCalendar()
            "WIFI" -> connectWifi()
            "ISBN" -> null
            "VIN" -> null
            else -> null
        }
    }



    private fun addAddressBook() {

    }

    private fun emailTo() {

    }

    private fun openUri() {

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

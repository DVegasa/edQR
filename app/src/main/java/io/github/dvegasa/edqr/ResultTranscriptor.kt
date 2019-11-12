package io.github.dvegasa.edqr

import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser

/**
 * 12.11.2019
 */

enum class QrType {
    BUSINESS_CARD, EMAIL, URI, TEXT, GEO, TEL, SMS, CALENDAR_EVENT, WIFI
}

class ResultTranscriptor(val rawResult: Result) {

    fun getText() = rawResult.text.toString()

    fun getType() =
        when (ResultParser.parseResult(rawResult).type.toString()) {
            "ADDRESSBOOK" -> QrType.BUSINESS_CARD
            "EMAIL_ADDRESS" -> QrType.EMAIL
            "PRODUCT" -> QrType.TEXT
            "URI" -> QrType.URI
            "TEXT" -> QrType.TEXT
            "GEO" -> QrType.GEO
            "TEL" -> QrType.TEL
            "SMS" -> QrType.SMS
            "CALENDAR" -> QrType.CALENDAR_EVENT
            "WIFI" -> QrType.WIFI
            "ISBN" -> QrType.TEXT
            "VIN" -> QrType.TEXT
            else -> QrType.TEXT
        }

    fun getTypeHint() =
        when (getType()) {
            QrType.BUSINESS_CARD -> "business card"
            QrType.EMAIL -> "e-mail"
            QrType.TEXT -> ""
            QrType.URI -> "web URL"
            QrType.GEO -> "geo point"
            QrType.TEL -> "telephone"
            QrType.SMS -> "SMS"
            QrType.CALENDAR_EVENT -> "calendar event"
            QrType.WIFI -> "Wi-Fi connection"
        }

    fun getActionHint() =
        when (getType()) {
            QrType.BUSINESS_CARD -> "ADD"
            QrType.EMAIL -> "WRITE"
            QrType.TEXT -> ""
            QrType.URI -> "OPEN"
            QrType.GEO -> "OPEN"
            QrType.TEL -> "ADD"
            QrType.SMS -> "SEND"
            QrType.CALENDAR_EVENT -> "ADD"
            QrType.WIFI -> "CONNECT"
        }
}



package io.github.dvegasa.edqr

/**
 * 12.11.2019
 */
class ActionHandler(val qrType: QrType) {

    fun doAction() {
        when (qrType) {
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

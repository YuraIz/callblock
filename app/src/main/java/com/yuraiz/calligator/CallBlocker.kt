package com.yuraiz.calligator

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.CallScreeningService


class CallBlocker : CallScreeningService() {

    private val callBlockIsOn: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("callBlockIsOn", false)

    private val rejectCallResponse =
        CallResponse.Builder().setDisallowCall(true).setRejectCall(true).build()

    override fun onScreenCall(callDetails: Call.Details) {
        if (callBlockIsOn && callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                if (isContact(callDetails)) {
                    return
                }
            }
            respondToCall(
                callDetails,
                rejectCallResponse
            )
        }
    }

    private fun isContact(details: Call.Details): Boolean {
        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            val phoneNumber = details.handle.schemeSpecificPart
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )

            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            return cursor.use {
                when (cursor!!.moveToFirst()) {
                    true -> true
                    else -> false
                }
            }
        } else {
            return false
        }
    }
}
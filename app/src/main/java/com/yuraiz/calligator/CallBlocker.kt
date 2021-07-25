package com.yuraiz.calligator

import android.telecom.Call
import android.telecom.CallScreeningService


class CallBlocker : CallScreeningService() {

    private val callBlockIsOn: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("callBlockIsOn", false)

    private val rejectCallResponse = CallResponse.Builder().setDisallowCall(true).setRejectCall(true).build()

    override fun onScreenCall(callDetails: Call.Details) {
        if (callBlockIsOn && callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {
            respondToCall(
                callDetails,
                rejectCallResponse
            )
        }
    }
}
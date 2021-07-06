package com.yuraiz.callblock

import android.telecom.Call
import android.telecom.CallScreeningService
import android.telephony.SmsManager
import android.util.Log

class CallBlocker : CallScreeningService() {

    companion object {
        var callBlockIsOn = true

        private var rejectCallResponse =
            CallResponse.Builder().setDisallowCall(true).setRejectCall(true).build()
    }

    override fun onScreenCall(callDetails: Call.Details) {
        if (callBlockIsOn && callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {
            respondToCall(
                callDetails,
                rejectCallResponse
            )
        }
    }
}
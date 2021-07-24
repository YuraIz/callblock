package com.yuraiz.callblock

import android.telecom.Call
import android.telecom.CallScreeningService


class CallBlocker : CallScreeningService() {

    private var callBlockIsOn: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("callBlockIsOn", false)

        set(preference) = getSharedPreferences("myPreferences", 0)
            .edit()
            .putBoolean("callBlockIsOn", preference)
            .apply()

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
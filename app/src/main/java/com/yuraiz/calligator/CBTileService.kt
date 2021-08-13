package com.yuraiz.calligator

import android.app.role.RoleManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class CBTileService : TileService() {

    private var callBlockIsOn: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("callBlockIsOn", false)

        set(preference) = getSharedPreferences("myPreferences", 0)
                .edit()
                .putBoolean("callBlockIsOn", preference)
                .apply()

    private fun isRoleHeld() =
        (getSystemService(ROLE_SERVICE) as RoleManager).isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

    override fun onStartListening() {
        qsTile.state = if (callBlockIsOn) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
        if (!isRoleHeld()) {
            qsTile.state = Tile.STATE_UNAVAILABLE
        }
        qsTile.updateTile()
        super.onStartListening()
    }

    private fun refreshMainActivity() {
        if(MainActivity.last != null) {
            MainActivity.last!!.updateButton(MainActivity.last!!.findViewById(R.id.switch_cb))
            MainActivity.last!!.updateText(MainActivity.last!!.findViewById(R.id.info))
        }
    }

    override fun onClick() {
        super.onClick()
        if (qsTile.state == Tile.STATE_INACTIVE) {
            qsTile.state = Tile.STATE_ACTIVE
            callBlockIsOn = true
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            callBlockIsOn = false
        }

        refreshMainActivity()

        qsTile.updateTile()
    }
}
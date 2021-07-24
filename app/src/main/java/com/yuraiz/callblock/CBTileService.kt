package com.yuraiz.callblock

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class CBTileService : TileService() {

    private var tileIsAdded: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("tileIsAdded", false)

        set(preference) =
            getSharedPreferences("myPreferences", 0)
                .edit()
                .putBoolean("tileIsAdded", preference)
                .apply()

    private var callBlockIsOn: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("callBlockIsOn", false)

        set(preference) = getSharedPreferences("myPreferences", 0)
                .edit()
                .putBoolean("callBlockIsOn", preference)
                .apply()

    override fun onStartListening() {
        if (callBlockIsOn) {
            qsTile.state = Tile.STATE_ACTIVE
        } else {
            qsTile.state = Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
        super.onStartListening()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        tileIsAdded = true
        MainActivity.update()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        tileIsAdded = false
        callBlockIsOn = false
        MainActivity.update()
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

        qsTile.updateTile()
    }
}
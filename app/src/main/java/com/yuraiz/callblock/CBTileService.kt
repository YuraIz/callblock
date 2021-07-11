package com.yuraiz.callblock

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class CBTileService : TileService() {

    companion object {
        var added = false
    }

    override fun onTileAdded() {
        super.onTileAdded()
        added = true
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        added = false
    }

    override fun onClick() {
        super.onClick()
        if (qsTile.state == Tile.STATE_INACTIVE) {
            qsTile.state = Tile.STATE_ACTIVE
            CallBlocker.callBlockIsOn = true
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            CallBlocker.callBlockIsOn = false
        }

        qsTile.updateTile()
    }
}
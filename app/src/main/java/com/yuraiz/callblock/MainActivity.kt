package com.yuraiz.callblock

import android.app.role.RoleManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private fun requestRole() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        startActivityForResult(intent, 1)
    }

    companion object{
        private var last : MainActivity? = null
        fun update() = last?.recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        last = this
        if (CBTileService.added) {
            setContentView(R.layout.all_done)
        } else {
            setContentView(R.layout.add_toggle)
        }
        requestRole()
    }
}
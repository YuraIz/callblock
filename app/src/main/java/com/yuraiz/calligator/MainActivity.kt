package com.yuraiz.calligator

import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class MainActivity : AppCompatActivity() {
    init {
        last = this
    }

    companion object {
        lateinit var last: MainActivity
    }

    private fun requestRole() =
        (getSystemService(ROLE_SERVICE) as RoleManager)
            .createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

    private fun isRoleHeld() =
        (getSystemService(ROLE_SERVICE) as RoleManager).isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

    private var callBlockIsOn: Boolean
        get() = getSharedPreferences("myPreferences", 0)
            .getBoolean("callBlockIsOn", false)
        set(preference) {
            getSharedPreferences("myPreferences", 0)
                .edit()
                .putBoolean("callBlockIsOn", preference)
                .apply()
        }

    override fun onResume() {
        super.onResume()
        updateButton(findViewById(R.id.switch_cb))
        updateText(findViewById(R.id.info))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.big_button)
        updateButton(findViewById(R.id.switch_cb))
        updateText(findViewById(R.id.info))
    }

    fun onOpenSourceLicensesClick(view: View) {
        startActivity(Intent(this, OssLicensesMenuActivity::class.java))
    }

    fun switchCallBlocking(view: View) {
        callBlockIsOn = !callBlockIsOn
        updateButton(view)
        updateText(findViewById(R.id.info))
        if (!isRoleHeld()) {
            startActivity(requestRole())
        }
    }

    fun updateButton(view: View) {
        (view as ImageButton).setImageResource(
            if (callBlockIsOn && isRoleHeld()) {
                R.drawable.ic_cb_on
            } else {
                R.drawable.ic_cb_off
            }
        )
    }

    fun updateText(view: View) {
        (view as TextView).text = if (isRoleHeld()) {
            if (callBlockIsOn) {
                getString(R.string.calligator_is_on)
            } else {
                getString(R.string.calligator_is_off)
            }
        } else {
            getString(R.string.calligator_is_not_default)
        }
    }

}
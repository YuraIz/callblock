package com.yuraiz.callblock

import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class MainActivity : AppCompatActivity() {

    private fun requestRole() =
        (getSystemService(ROLE_SERVICE) as RoleManager)
            .createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

    companion object {
        private var last: MainActivity? = null
        fun update() = last?.recreate()
    }

    private val tileIsAdded: Boolean
        get() =
            getSharedPreferences("myPreferences", 0)
                .getBoolean("tileIsAdded", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        last = this
        if (tileIsAdded) {
            setContentView(R.layout.all_done)
        } else {
            setContentView(R.layout.add_toggle)
        }
        requestRole()
    }

    fun onOpenSourceLicensesClick(view: View) =
        startActivity(Intent(this, OssLicensesMenuActivity::class.java))
}
package com.yuraiz.calligator

import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity


class MainActivity : AppCompatActivity() {
    init {
        last = this
    }

    companion object {
        var last: MainActivity? = null
    }

    private fun isRoleHeld() =
        (getSystemService(ROLE_SERVICE) as RoleManager).isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

    private fun requestRole() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        resultLauncher.launch(intent)
    }

    private fun contactsPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
               if( ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_CONTACTS
                )) {
                   (this.let {
                       val builder = AlertDialog.Builder(it)
                       builder.setTitle(R.string.read_contacts)
                           .setMessage(R.string.read_contacts_rationale)
                           .setPositiveButton("OK") { _, _ ->
                               ActivityCompat.requestPermissions(
                                   this,
                                   arrayOf<String>(android.Manifest.permission.READ_CONTACTS),
                                   0,
                               )
                           }
                       builder.create()
                   }).show()
               }
            }
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ -> }


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
        if (!isRoleHeld()) {
            requestRole()
        }
        contactsPermission()
    }

    fun onOpenSourceLicensesClick(view: View) {
        startActivity(Intent(this, OssLicensesMenuActivity::class.java))
    }

    fun switchCallBlocking(view: View) {
        callBlockIsOn = !callBlockIsOn
        updateButton(view)
        updateText(findViewById(R.id.info))
        if (!isRoleHeld()) {
            requestRole()
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
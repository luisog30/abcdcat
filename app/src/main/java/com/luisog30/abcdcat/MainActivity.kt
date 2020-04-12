package com.luisog30.abcdcat

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isNetworkAvailable()) {

            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this)

            builder.setTitle(R.string.no_internet_title_string)

            builder.setMessage(R.string.internet_connection_string)

            builder.setCancelable(false)

            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> this.finish()
                }
            }

            builder.setPositiveButton("OK", dialogClickListener)

            dialog = builder.create()

            dialog.show()

        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_bar_button, menu)
        menu!!.setGroupVisible(R.id.ranking_group, false)

        return true

    }

}

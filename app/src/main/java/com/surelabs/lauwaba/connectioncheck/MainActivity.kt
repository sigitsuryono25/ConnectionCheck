package com.surelabs.lauwaba.connectioncheck

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.surelabs.lauwaba.connectioncheck.utils.NetworkMonitoring
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mNetworkMonitoring = NetworkMonitoring()

    //ini buat nyaring broadcast dari NetworkMonitoring
    private val bor = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "connectionBroadcast") {
                val bu = intent.getBundleExtra("wifiBroadcast")
                val c = bu?.getString("connectionName")

                //set ke textview
                networkInfo.text = c
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        registerReceiver(bor, IntentFilter("connectionBroadcast"))
    }

    private fun requestPermission() {
        val checkPermission = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            registerNetworkMonitoring()
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 500
                )
            }
        }
    }

    private fun registerNetworkMonitoring() {
        registerReceiver(
            mNetworkMonitoring,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 500) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerNetworkMonitoring()
            }
        }
    }
}
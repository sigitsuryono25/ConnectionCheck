package com.surelabs.lauwaba.connectioncheck.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log

/* *
* ini dipake untuk monitoring network
* jangan lupa didaftarkan di manifest
* */
class NetworkMonitoring : BroadcastReceiver() {

    private val b: Bundle = Bundle()

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (ConnectivityManager.CONNECTIVITY_ACTION == action) {
            val cm =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.state == NetworkInfo.State.CONNECTED) {
                val wifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val mWifiInfo = wifiManager.connectionInfo
                b.putString("connectionName", mWifiInfo.ssid)
            } else {
                val mTelephonyManager =
                    context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                b.putString("connectionName", mTelephonyManager.networkOperatorName)
                Log.d("mobILE", mTelephonyManager.networkOperatorName)
            }
        }

        val mBroadcast = Intent("connectionBroadcast")
        mBroadcast.putExtra("wifiBroadcast", b)
        context.sendBroadcast(mBroadcast)
    }

}

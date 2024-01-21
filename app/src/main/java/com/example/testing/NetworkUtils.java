package com.example.testing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {
    public static final int WIFI_STRENGTH_STRONG = 3;
    public static final int WIFI_STRENGTH_MODERATE = 2;
    public static final int WIFI_STRENGTH_WEAK = 1;

    public static int getWifiStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int signalStrength = wifiInfo.getRssi();
        int numberOfLevels = 5; // Số mức tín hiệu WiFi

        if (WifiManager.calculateSignalLevel(signalStrength, numberOfLevels) == 4) {
            return WIFI_STRENGTH_STRONG;
        } else if (WifiManager.calculateSignalLevel(signalStrength, numberOfLevels) >= 2) {
            return WIFI_STRENGTH_MODERATE;
        } else {
            return WIFI_STRENGTH_WEAK;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}

package com.elroid.wirelens.util;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.util.WifiUtils
 * Project: WireLens
 * Created Date: 24/01/2018 17:58
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class WifiUtils
{
	public static void openWifiSettings(Context ctx){
		Intent in = new Intent(Settings.ACTION_WIFI_SETTINGS);
		ctx.startActivity(in);
	}

	public static void checkWifiEnabled(WifiManager wifiManager) throws Exception{
		if(!wifiManager.isWifiEnabled()){
			Timber.d("Enabling wi-fi...");
			if(wifiManager.setWifiEnabled(true)){
				Timber.d("Wi-fi set to enabled");
			}
			else{
				throw new Exception("Wi-fi could not be enabled!");
			}
			// This happens very quickly, but need to wait for it to enable. A
			// little busy wait?
			int count = 0;
			while(!wifiManager.isWifiEnabled()){
				if(count >= 60){
					throw new Exception("Took too long to enable wi-fi, quitting");
				}
				Timber.d("Still waiting for wi-fi to enable...");
				try{
					Thread.sleep(1000L);
				}
				catch(InterruptedException ie){
					// continue
				}
				count++;
			}
		}
		else Timber.v("WiFi is already enabled");
	}

	public static int getSignalStrengthPercent(WifiManager wifiManager){
		int numberOfLevels = 100;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
	}

	public static boolean isValidIp(String ip){
		if(ip == null) return false;
		if(ip.equals("0.0.0.0")) return false;
		if(ip.startsWith("169.254")) return false;
		return true;
	}

	public static boolean isSameSsid(String ssid1, String ssid2){
		String naked1 = ssid1.replace("\"", "");
		String naked2 = ssid2.replace("\"", "");
		return TextUtils.equalsIgnoreCase(naked1, naked2);
	}
}

package com.elroid.wirelens.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.model.WifiNetwork
 * Project: WireLens
 * Created Date: 24/01/2018 10:12
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class WifiNetwork
{
	private String ssid;
	private String capabilities;
	private int signalLevel;

	public WifiNetwork(ScanResult scanResult){
		Timber.d("WifiNetwork(scanResult:%s)", scanResult);

		this.ssid = scanResult.SSID;
		this.capabilities = scanResult.capabilities;
		this.signalLevel = scanResult.level;
	}

	public WifiNetwork(String ssid, int signalLevel){
		this.ssid = ssid;
		this.signalLevel = signalLevel;
	}

	public WifiNetwork(String ssid, String capabilities, int signalLevel){
		this.ssid = ssid;
		this.capabilities = capabilities;
		this.signalLevel = signalLevel;
	}

	public String getSsid(){
		return ssid;
	}

	public int getSignalLevel(){
		return signalLevel;
	}

	public String getCapabilities(){
		return capabilities;
	}

	@Override public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof WifiNetwork)) return false;

		WifiNetwork that = (WifiNetwork) o;

		return ssid.equals(that.ssid);
	}

	@Override public int hashCode(){
		return ssid.hashCode();
	}

	@Override public String toString(){
		final StringBuilder sb = new StringBuilder("WifiNetwork{");
		sb.append("ssid='").append(ssid).append('\'');
		sb.append(", capabilities='").append(capabilities).append('\'');
		sb.append(", signalLevel=").append(signalLevel);
		sb.append('}');
		return sb.toString();
	}
}

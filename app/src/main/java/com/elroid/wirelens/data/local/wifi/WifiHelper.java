package com.elroid.wirelens.data.local.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.util.GenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

import static android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;

/**
 * Class: com.elroid.wirelens.data.local.wifi.WifiHelper
 * Project: WireLens
 * Created Date: 24/01/2018 10:26
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class WifiHelper implements WifiDataManager
{
	private final WifiManager wifiManager;
	private final Context ctx;

	@Inject
	public WifiHelper(WifiManager wifiManager, Context ctx){
		this.wifiManager = wifiManager;
		this.ctx = ctx;
	}

	@Override
	public Single<List<WifiNetwork>> scan(){
		boolean started = wifiManager.startScan();
		Timber.d("started scan: %s", started);
		return Single.create(emitter -> {
			BroadcastReceiver receiver = new BroadcastReceiver()
			{
				@Override
				public void onReceive(Context context, Intent intent){
					Timber.i("received scan results intent: %s", intent);
					List<ScanResult> unsortedResults = wifiManager.getScanResults();
					Timber.i("unsorted results are: %s", unsortedResults);
					List<WifiNetwork> result = new ArrayList<>();
					for(int i = 0; i < unsortedResults.size(); i++){
						ScanResult scanResult = unsortedResults.get(i);
						WifiNetwork wn = new WifiNetwork(scanResult);
						if(GenUtils.isBlank(wn.getSsid())){
							Timber.w("this network has a blank SSID: %s", scanResult);
						}
						else if(result.contains(wn)){
							Timber.d("list already contains: %s", wn);
						}
						else{
							result.add(wn);
						}
					}
					Collections.sort(result, (o1, o2)
						-> Integer.valueOf(o2.getSignalLevel()).compareTo(o1.getSignalLevel()));
					Timber.i("final results are: %s", result);
					ctx.unregisterReceiver(this);
					emitter.onSuccess(result);
				}
			};
			ctx.registerReceiver(receiver, new IntentFilter(SCAN_RESULTS_AVAILABLE_ACTION));
		});
	}

	@Override
	public Completable connect(String ssid, String password){
		Timber.d("connect(ssid:%s, password:%s)", ssid, password);
		return Completable.create(emitter -> {
			int netId = -1;
			try{
				// Create config
				WifiConfiguration config = new WifiConfiguration();
				// Must be in double quotes to tell system this is an ASCII SSID and passphrase.
				config.SSID = String.format("\"%s\"", ssid);
				if(GenUtils.isBlank(password))
					config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				else
					config.preSharedKey = String.format("\"%s\"", password);

				/*
				//for WEP network you need to do this:
				conf.wepKeys[0] = "\"" + networkPass + "\"";
				conf.wepTxKeyIndex = 0;
				conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
				//Note: If your password is in hex, you do not need to surround it with quotes.
				 */

				//Timber.d("(before) configured networks: %s", printConfiguredNetworks());

				//add network
				netId = wifiManager.addNetwork(config);
				if(netId != -1){
					boolean success = wifiManager.enableNetwork(netId, true);
					if(success){
						Timber.i("WiFi network '%s' is enabled", ssid);

						//wait for appropriate connnected state
						WifiInfo wifiInfo = wifiManager.getConnectionInfo();
						SupplicantState supplicantState = wifiInfo.getSupplicantState();
						Timber.i("supplicant state is %s", supplicantState);
						while(supplicantState != SupplicantState.COMPLETED){
							Thread.sleep(500);
							wifiInfo = wifiManager.getConnectionInfo();
							supplicantState = wifiInfo.getSupplicantState();
							Timber.i("supplicant state is still %s", supplicantState);
							if(supplicantState == SupplicantState.DISCONNECTED)
								throw new Exception("Connection failed");
						}
						emitter.onComplete();
					}
					else{
						throw new Exception("An error occurred enabling/connecting to network");
					}
				}
				else{
					throw new Exception("Unknown error while calling WiFiManager.addNetwork()");
				}
			}
			catch(Exception e){
				Timber.e(e, "Error connecting to network");
				if(netId > -1){
					Timber.w("Forgetting network...");
					wifiManager.removeNetwork(netId);
				}
				emitter.onError(e);
			}
			Timber.d("(after) configured networks: %s", printConfiguredNetworks());
		});
	}

	private String printConfiguredNetworks(){
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		StringBuilder r = new StringBuilder();
		for(int i = 0; i < configs.size(); i++){
			WifiConfiguration wc = configs.get(i);
			if(i != 0) r.append("\n");
			r.append(String.format("'%s' (%s)", wc.SSID, wc.networkId));
		}
		return r.toString();
	}
}

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
import android.text.format.Formatter;

import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.util.GenUtils;
import com.elroid.wirelens.util.Printer;
import com.elroid.wirelens.util.WifiUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
	public Observable<List<WifiNetwork>> scan(){
		boolean started = wifiManager.startScan();
		Timber.d("started scan: %s", started);
		return Observable.create(emitter -> {
			emitter.onNext(parseResults(wifiManager.getScanResults()));
			BroadcastReceiver receiver = new BroadcastReceiver()
			{
				@Override
				public void onReceive(Context context, Intent intent){
					Timber.i("received scan results intent: %s", intent);
					List<ScanResult> unsortedResults = wifiManager.getScanResults();
					ctx.unregisterReceiver(this);
					emitter.onNext(parseResults(unsortedResults));
					emitter.onComplete();
				}
			};
			ctx.registerReceiver(receiver, new IntentFilter(SCAN_RESULTS_AVAILABLE_ACTION));
		});
	}

	private List<WifiNetwork> parseResults(List<ScanResult> unsortedResults){
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
		return result;
	}

	@Override
	public Completable connect(String ssid, String password){
		Timber.d("connect(ssid:%s, password:%s) on thread:%s",
			ssid, password, Printer.printThread());
		return Completable.create(emitter -> {
			int netId = -1;
			try{

				//check current state
				String currentSsid = getCurrentSsid();
				if(currentSsid != null){
					Timber.i("already connected to %s", currentSsid);
					if(WifiUtils.isSameSsid(ssid, currentSsid)){
						Timber.w("We are already connected to %s", ssid);
						netId = wifiManager.getConnectionInfo().getNetworkId();
					}
					else{
						Timber.i("We're connected to '%s' but it's not '%s' so let's continue...",
							currentSsid, ssid);
					}
				}

				if(netId == -1){
					WifiConfiguration config;
					//todo check if it's already saved?

					// Create config
					config = new WifiConfiguration();
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
				}
				if(netId != -1){
					boolean success = wifiManager.enableNetwork(netId, true);
					if(success){
						Timber.i("WiFi network '%s' is enabled", ssid);

						//wait for appropriate connnected state
						WifiInfo wifiInfo = wifiManager.getConnectionInfo();
						SupplicantState supplicantState = wifiInfo.getSupplicantState();
						Timber.i("supplicant state is %s and ip is %s",
							supplicantState, getCurrentIp());

						//todo wrong password shows complete!!

						while(supplicantState != SupplicantState.COMPLETED){
							Thread.sleep(500);
							wifiInfo = wifiManager.getConnectionInfo();
							supplicantState = wifiInfo.getSupplicantState();
							if(supplicantState == SupplicantState.COMPLETED)
								Timber.i("supplicant state is COMPLETED!");
							else if(supplicantState == SupplicantState.DISCONNECTED)
								throw new Exception("Connection failed (disconnected)");
							else
								Timber.i("supplicant state is still %s and ip is %s",
								supplicantState, getCurrentIp());

							/*if(supplicantState == SupplicantState.SCANNING)
								throw new Exception("Connection failed (scanning agin)");*/
						}
					}
					else{
						throw new Exception("An error occurred enabling/connecting to network");
					}
				}
				else{
					throw new Exception("Unknown error while calling WiFiManager.addNetwork()");
				}

				String ip;
				while(!WifiUtils.isValidIp(ip = getCurrentIp())){
					Thread.sleep(500);
					Timber.i("wating for ip (still is %s)...", ip);
				}
				Timber.i("got ip: %s", ip);

				int NUM_ATTEMPTS = 5;
				int INCR = 250;
				int delay = 500;
				for(int i = 0; i < NUM_ATTEMPTS; i++){
					boolean lastTime = i == NUM_ATTEMPTS - 1;
					Timber.d("checking connection...");
					try{
						checkConn();
						break;
					}
					catch(UnknownHostException e){
						if(lastTime){
							throw e;
						}
						else{
							Timber.w("Unable to connect (unknown host)");
							Thread.sleep(delay);
						}
					}
					catch(Exception e){
						if(lastTime){
							throw e;
						}
						else{
							Timber.w("Unable to connect: %s", e.getMessage());
							Thread.sleep(delay);
						}
					}
					delay += INCR;
				}
				emitter.onComplete();
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

	@SuppressWarnings("deprecation") //todo we should fix this at some point...
	private String getCurrentIp(){
		try{
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ip = wifiInfo.getIpAddress();
			return Formatter.formatIpAddress(ip);
		}
		catch(Exception e){
			Timber.w(e, "Error getting current ip");
			return "UNKNOWN";
		}
	}

	private String getCurrentSsid(){
		try{
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String result = wifiInfo.getSSID();
			if(result.equals("0x")){
				Timber.d("got 0x as ssid, treating as null...");
				return null;
			}
			else
				return result;
		}
		catch(Exception e){
			Timber.w(e, "Error getting current ssid");
			return null;
		}
	}

	private void checkConn() throws Exception{
		String url = "http://elroid.com/wirelens/test.php";
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		ResponseBody body = response.body();
		if(body == null) throw new Exception("body is empty");
		String result = body.string();
		Timber.i("got result from test page: %s", result);
	}
	/*private Completable checkConnCompletable(){
		return Completable.create(emitter -> {
			try{
				checkConn();
				emitter.onComplete();
			}
			catch(Exception e){
				Timber.w(e, "Error connecting to test page");
				emitter.onError(e);
			}
		});
	}*/
}

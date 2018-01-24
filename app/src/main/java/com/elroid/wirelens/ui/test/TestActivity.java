package com.elroid.wirelens.ui.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.elroid.wirelens.R;
import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.ui.base.BaseActivity;
import com.elroid.wirelens.ui.base.SchedulersFacade;
import com.elroid.wirelens.util.Printer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.ui.test.TestActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:51
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class TestActivity extends BaseActivity
{
	private static final int PERMISSIONS_REQUEST_CODE = 23;

	public static Intent createIntent(Context ctx){
		return new Intent(ctx, TestActivity.class);
	}

	@Inject WifiManager wifiManager;
	@Inject WifiDataManager wifiDataManager;
	@Inject SchedulersFacade schedulers;

	TextView narf;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_java);

		narf = findViewById(R.id.narf);

		/*List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
		for(int i = 0; i < configuredNetworks.size(); i++){
			WifiConfiguration wifiConfiguration = configuredNetworks.get(i);
			Timber.d("config[%s]: %s", i, wifiConfiguration);
			if(i != 0) r.append("\n");
			r.append(wifiConfiguration.SSID);
			//if (allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
		}*/

		/*WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int numberOfLevels = 5;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);*/

		/*List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
		for(int i = 0; i < configuredNetworks.size(); i++){
			WifiConfiguration wifiConfiguration = configuredNetworks.get(i);
			Timber.d("config[%s]: %s", i, wifiConfiguration);
			if(i != 0) r.append("\n");
			r.append(wifiConfiguration.SSID);
			//if (allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
		}*/

		//String permission = Manifest.permission.CHANGE_WIFI_STATE;
		String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
		Button button = findViewById(R.id.button);
		button.setOnClickListener(view -> {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(TestActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
				Timber.d("requesting permission: %s", permission);
				if(shouldShowRequestPermissionRationale(permission)){
					Timber.d("needing rationale!!");
				}
				ActivityCompat.requestPermissions(TestActivity.this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
				//After this point you wait for callback in onRequestPermissionsResult

			}
			else{
				Timber.d("permission is already granted");
				scan();
				//do something, permission was previously granted; or legacy device
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults){
		Timber.d("onRequestPermissionsResult(requestCode:%s, permissions:%s, grantResults:%s)",
			requestCode, Printer.print(permissions), Printer.print(grantResults));
		if(requestCode == PERMISSIONS_REQUEST_CODE
			&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			Timber.d("permission granted: %s", permissions);
			// Do something with granted permission
			scan();
		}
		else{
			Timber.w("why didn't this work?!");
		}
	}


	private void scan(){
		wifiDataManager.scan()
			.subscribeOn(schedulers.io())
			.observeOn(schedulers.ui())
			.subscribe(new Observer<WifiNetwork>()
			{
				@Override
				public void onSubscribe(Disposable d){
					Timber.d("subscribed to scan");
				}

				@Override
				public void onNext(WifiNetwork wifiNetwork){
					Timber.d("got wifi result: %s", wifiNetwork);
					//narf.append("\n"+String.format("'%s' (%s) at %sdb", wifiNetwork.getSsid(), wifiNetwork.getCapabilities(), wifiNetwork.getSignalLevel()));
					narf.append("\n" + String.format("'%s' at %sdb", wifiNetwork.getSsid(),
						wifiNetwork.getSignalLevel()));
				}

				@Override
				public void onError(Throwable e){
					Timber.w(e, "Scan error happened");
				}

				@Override
				public void onComplete(){
					Timber.d("scan complete");
				}
			});
	}
}

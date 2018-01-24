package com.elroid.wirelens.ui.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.elroid.wirelens.R;
import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.ui.base.BaseActivity;
import com.elroid.wirelens.ui.base.SchedulersFacade;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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

		Button button = findViewById(R.id.button);
		button.setOnClickListener(view -> scanWithPermissionsCheck());
	}

	private void scanWithPermissionsCheck(){

		MultiplePermissionsListener dialogListener =
			DialogOnAnyDeniedMultiplePermissionsListener.Builder
				.withContext(this)
				.withTitle("Wifi & course location permission")
				.withMessage("Both change wifi state and coarse location permissions are needed to scan for wifi")
				.withButtonText(android.R.string.ok)
				.withIcon(R.mipmap.ic_launcher)
				.build();

		BaseMultiplePermissionsListener actionListener = new BaseMultiplePermissionsListener()
		{
			@Override public void onPermissionsChecked(MultiplePermissionsReport report){
				if(report.areAllPermissionsGranted()){
					scan();
				}
			}
		};

		Dexter.withActivity(this)
			.withPermissions(
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.CHANGE_WIFI_STATE)
			.withListener(new CompositeMultiplePermissionsListener(dialogListener, actionListener))
			.check();
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
					//narf.append("\n"+String.format("'%s' (%s) at %sdb", wifiNetwork.getSsid(),
					// wifiNetwork.getCapabilities(), wifiNetwork.getSignalLevel()));
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

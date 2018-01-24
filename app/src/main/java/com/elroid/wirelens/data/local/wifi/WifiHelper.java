package com.elroid.wirelens.data.local.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.WifiNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
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
	public Observable<WifiNetwork> scan(){
		boolean started = wifiManager.startScan();
		Timber.d("started scan: %s", started);
		return RxReceivers.from(SCAN_RESULTS_AVAILABLE_ACTION, ctx).flatMapIterable(intent -> {
			Timber.i("received scan results intent: %s", intent);
			//return wifiManager.getScanResults();
			List<ScanResult> unsortedResults = wifiManager.getScanResults();
			Timber.i("unsorted results are: %s", unsortedResults);
			List<WifiNetwork> result = new ArrayList<>();
			for(int i = 0; i < unsortedResults.size(); i++){
				ScanResult scanResult = unsortedResults.get(i);
				WifiNetwork wn = new WifiNetwork(scanResult);
				if(result.contains(wn)){
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
		});
	}

	@Override
	public Completable connect(WifiNetwork network, String password){
		Timber.w("connect(network:%s, password:%s)", network, password);
		Timber.w("not yet implemented");
		return null;
	}
}

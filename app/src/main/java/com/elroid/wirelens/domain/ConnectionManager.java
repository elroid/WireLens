package com.elroid.wirelens.domain;

import android.graphics.Bitmap;

import com.elroid.wirelens.model.ConnectionAttempt;
import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.GoogleVisionResponse;
import com.elroid.wirelens.model.TextParserResponse;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.util.GenUtils;
import com.elroid.wirelens.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.domain.ConnectionManager
 * Project: WireLens
 * Created Date: 25/01/2018 17:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ConnectionManager
{
	private final DataManager dataManager;
	private final TextParser textParser;
	private final WifiDataManager wifiManager;

	@Inject
	public ConnectionManager(DataManager dataManager, TextParser textParser, WifiDataManager wifiManager){
		this.dataManager = dataManager;
		this.textParser = textParser;
		this.wifiManager = wifiManager;
	}

	private String verifySSID(List<WifiNetwork> networks, String parsedSsid){
		for(int i = 0; i < networks.size(); i++){
			WifiNetwork wifiNetwork = networks.get(i);
			//todo ideally this should be somewhat fuzzy
			if(TextUtils.equalsIgnoreCase(parsedSsid, wifiNetwork.getSsid()))
				return wifiNetwork.getSsid();
		}
		return null;
	}

	public Observable<ConnectionAttempt> connect(CredentialsImage image/*, boolean fuzzyPassword*/){
		Observable<TextParserResponse> textParseResult = dataManager.extractText(image)
			.toObservable()
			.flatMap(textParser::parseResponse);
		return connect(textParseResult);
	}

	public Observable<ConnectionAttempt> connect(Observable<TextParserResponse> textParseResult/*, boolean fuzzyPassword*/){

		/*Observable<TextParserResponse> textParseResult = dataManager.extractText(image)
			.toObservable().flatMap(textParser::parseResponse);*/
		Observable<List<WifiNetwork>> networksAvailable = wifiManager.scan().toObservable();

		return Observable.combineLatest(textParseResult,
			networksAvailable,
			(tpr, wifiNetworks) -> {
				Timber.d("try combo (textParserResponse:%s, wifiNetworks:%s)", tpr, wifiNetworks);
				if(tpr.hasBoth()){
					Timber.d("Yay! we found ssid and password in image. Verifying ssid...");
					//ideal! we have something to try, check it against the available ssid
					String verifiedSSID = verifySSID(wifiNetworks, tpr.getSsid());
					if(!GenUtils.isBlank(verifiedSSID)){
						//sweet - everything is perfect
						Timber.d("SSID %s verified!", verifiedSSID);
						return Collections.singletonList(new ConnectionAttempt(verifiedSSID, tpr.getPassword()));
					}
					else{
						//hmm, can't verify the ssid....try it anyway?
						Timber.w("SSID was not found: %s (trying anyway)", tpr.getSsid());
						return Collections.singletonList(new ConnectionAttempt(tpr.getSsid(), tpr.getPassword()));
					}
				}
				else if(tpr.hasPassword()){
					Timber.d("image seems to have a password at least - trying nearby networks...");
					int MAX = 3;
					//try the password with the top MAX networks
					List<ConnectionAttempt> attempts = new ArrayList<>(MAX);
					for(int i = 0; i < wifiNetworks.size(); i++){
						WifiNetwork wifiNetwork = wifiNetworks.get(i);
						attempts.add(new ConnectionAttempt(wifiNetwork.getSsid(), tpr.getPassword()));
						if(attempts.size() >= MAX)
							break;
					}
					return attempts;
				}
				else if(tpr.hasSsid()){
					Timber.d("we found an ssid but no password! Assuming %s is an open network...", tpr.getSsid());
					String verifiedSSID = verifySSID(wifiNetworks, tpr.getSsid());
					if(!GenUtils.isBlank(verifiedSSID)){
						//sweet - everything is perfect
						Timber.d("Open SSID %s verified", verifiedSSID);
						return Collections.singletonList(new ConnectionAttempt(verifiedSSID, ""));
					}
					else{
						//hmm, can't verify the ssid....try it anyway?
						Timber.w("SSID was not found: %s (trying open anyway)", tpr.getSsid());
						return Collections.singletonList(new ConnectionAttempt(tpr.getSsid(), ""));
					}

				}
				else{
					Timber.w("we found neither an ssid nor a password in this image... throwing an error");
					throw new Exception("No credentials found in image");
				}
			}).flatMapIterable(connectionAttempts -> connectionAttempts);
	}
}

package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.ConnectionAttempt;
import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.TextParserResponse;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.util.FuzzyTextUtils;
import com.elroid.wirelens.util.GenUtils;
import com.elroid.wirelens.util.Printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.domain.ConnectionManager
 * Project: WireLens
 * Created Date: 25/01/2018 17:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ConnectionGuesser {
	private final DataManager dataManager;
	private final TextParser textParser;
	private final WifiDataManager wifiManager;

	@Inject
	public ConnectionGuesser(DataManager dataManager, TextParser textParser,
							 WifiDataManager wifiManager){
		this.dataManager = dataManager;
		this.textParser = textParser;
		this.wifiManager = wifiManager;
	}

	private String verifySSID(List<WifiNetwork> networks, String parsedSsid){
		for(int i = 0; i < networks.size(); i++){
			WifiNetwork wifiNetwork = networks.get(i);
			Timber.v("checking %s against %s...", parsedSsid, wifiNetwork.getSsid());
			if(FuzzyTextUtils.matches(wifiNetwork.getSsid(), parsedSsid, 70)){
				Timber.v("found match!");
				return wifiNetwork.getSsid();
			}
		}
		Timber.v("no matches found");
		return null;
	}

	public Observable<ConnectionAttempt> guess(CredentialsImage image/*, boolean fuzzyPassword*/){
		Observable<TextParserResponse> textParseResult = dataManager.extractText(image)
			.toObservable()
			.flatMap(textParser::parseResponse);
		return guess(textParseResult);
	}

	public Observable<ConnectionAttempt> guess(
		Observable<TextParserResponse> textParseResult/*, boolean fuzzyPassword*/){
		Timber.d("connect(textParseResult:%s) onThread:%s", textParseResult, Printer.printThread());

		/*Observable<TextParserResponse> textParseResult = dataManager.extractText(image)
			.toObservable().flatMap(textParser::parseResponse);*/
		Observable<List<WifiNetwork>> networksAvailable = wifiManager.scan();

		return Observable.combineLatest(textParseResult,
			networksAvailable,
			(BiFunction<TextParserResponse, List<WifiNetwork>, List<ConnectionAttempt>>)(tpr, wifiNetworks) -> {
				Timber.d("try combo (textParserResponse:%s, wifiNetworks:%s)", tpr, wifiNetworks);
				if(tpr.hasBoth()){
					Timber.d("Yay! we found ssid and password in image. Verifying ssid...");
					//ideal! we have something to try, check it against the available ssid
					String verifiedSSID = verifySSID(wifiNetworks, tpr.getSsid());
					if(!GenUtils.isBlank(verifiedSSID)){
						//sweet - everything is perfect
						Timber.d("SSID %s verified!", verifiedSSID);
						return Collections.singletonList(
							new ConnectionAttempt(verifiedSSID, tpr.getPassword()));
					}
					else{
						//hmm, can't verify the ssid....try it anyway?
						//Timber.w("SSID was not found: %s (trying anyway)", tpr.getSsid());
						//return Collections.singletonList(new ConnectionAttempt(tpr.getSsid(), tpr.getPassword()));
						Timber.w("Unable to find wifi network '" + tpr.getSsid() + "'");
						//throw new Exception("Unable to find wifi network '" + tpr.getSsid() + "'");
						return Collections.emptyList();
					}
				}
				else if(tpr.hasPassword()){
					Timber.d("image seems to have a password at least - trying nearby networks...");
					int MAX = 3;
					//try the password with the top MAX networks
					List<ConnectionAttempt> attempts = new ArrayList<>(MAX);
					for(int i = 0; i < wifiNetworks.size(); i++){
						WifiNetwork wifiNetwork = wifiNetworks.get(i);
						attempts.add(
							new ConnectionAttempt(wifiNetwork.getSsid(), tpr.getPassword()));
						if(attempts.size() >= MAX)
							break;
					}
					return attempts;
				}
				else if(tpr.hasSsid()){
					Timber.d("we found an ssid but no password! Assuming %s is an open network...",
						tpr.getSsid());
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
					Timber.w(
						"we found neither an ssid nor a password in this image... throwing an error");
					throw new Exception("No credentials found in image");
				}
			}).flatMapIterable(connectionAttempts -> connectionAttempts);

	}
}

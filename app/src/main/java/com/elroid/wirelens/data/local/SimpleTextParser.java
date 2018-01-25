package com.elroid.wirelens.data.local;

import com.elroid.wirelens.domain.TextParser;
import com.elroid.wirelens.model.GoogleVisionResponse;
import com.elroid.wirelens.model.TextParserResponse;
import com.elroid.wirelens.util.TextUtils;

import io.reactivex.Observable;

/**
 * Class: com.elroid.wirelens.data.local.SimpleTextParser
 * Project: WireLens
 * Created Date: 23/01/2018 15:10
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class SimpleTextParser implements TextParser
{
	@Override
	public Observable<TextParserResponse> parseResponse(GoogleVisionResponse gvr){
		return Observable.combineLatest(
			getSSID(gvr).defaultIfEmpty(""),
			getPassword(gvr).defaultIfEmpty(""),
			(ssid, password) -> new TextParserResponse(gvr.getText(), ssid, password));
	}

	public Observable<String> getSSID(GoogleVisionResponse gvr){
		return getValue(gvr, ssidTokens);
	}

	private Observable<String> getPassword(GoogleVisionResponse gvr){
		return getValue(gvr, passwordTokens);
	}

	private static final String[] ssidTokens = new String[]{"ssid", "wifi ssid"};
	private static final String[] passwordTokens = new String[]{"Password", "wpa-psk"};

	private Observable<String> getValue(GoogleVisionResponse gvr, String[] tokens){
		return Observable.create(emitter -> {
			try{
				for(String token : tokens){
					if(TextUtils.containsIgnoreCase(gvr.getText(), token)){
						String value = TextUtils.getValueFromLineStartingWith(token, gvr.getLines());
						emitter.onNext(value);
					}
				}
				emitter.onComplete();
			}
			catch(Exception e){
				emitter.onError(e);
			}
		});
	}
}

package com.elroid.wirelens.data.local;

import com.elroid.wirelens.domain.TextParser;
import com.elroid.wirelens.model.GoogleVisionResponse;
import com.elroid.wirelens.model.TextParserResponse;

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
		return parseBoth(gvr);
	}

	public Observable<TextParserResponse> parseBoth(GoogleVisionResponse gvr){
		return Observable.combineLatest(getSSID(gvr), getPassword(gvr), (ssid, password) ->
			new TextParserResponse(gvr.getText(), ssid, password));
	}

	public Observable<String> getSSID(GoogleVisionResponse gvr){
		return getValue(gvr, ssidTokens);
	}
	public Observable<String> getPassword(GoogleVisionResponse gvr){
		return getValue(gvr, passwordTokens);
	}

	private static final String[] ssidTokens = new String[]{"ssid", "wifi ssid"};
	private static final String[] passwordTokens = new String[]{"Password"};

	public Observable<String> getValue(GoogleVisionResponse gvr, String[] tokens){
		return Observable.create(emitter -> {
			try{
				for(String token : tokens){
					if(containsIgnoreCase(gvr.getText(), token)){
						String value = getValueFromLineStartingWith(token, gvr.getLines());
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

	public static String getValueFromLineStartingWith(String label, String[] lines){
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			int index;
			if((index = indexOfIgnoreCase(line, label)) != -1){
				String tail = line.substring(index + label.length()).trim();
				while(tail.startsWith(":"))
					tail = tail.substring(1);
				if(tail.trim().equals("")){
					//assume it is the next line
					if(i+1 < lines.length){
						return lines[i+1].trim();
					}
					else
						return "";
				}
				return tail.trim();
			}
		}
		return null;
	}

	private static int indexOfIgnoreCase(String str, String tok){
		String strUpper = str.toUpperCase();
		String tokUpper = tok.toUpperCase();
		return strUpper.indexOf(tokUpper);
	}

	private static boolean containsIgnoreCase(String str, String tok){
		return indexOfIgnoreCase(str, tok) > 0;
	}
}

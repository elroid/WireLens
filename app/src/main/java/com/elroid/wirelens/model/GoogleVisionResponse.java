package com.elroid.wirelens.model;

import org.json.JSONObject;

/**
 * Class: com.elroid.wirelens.model.GoogleVisionResponse
 * Project: WireLens
 * Created Date: 18/01/2018 13:25
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class GoogleVisionResponse
{
	private String text;
	private String[] lines;

	public GoogleVisionResponse(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public String[] getLines(){
		if(lines == null){
			lines = text.split("\n");
		}
		return lines;
	}
}
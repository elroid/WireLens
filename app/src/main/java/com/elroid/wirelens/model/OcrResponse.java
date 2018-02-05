package com.elroid.wirelens.model;

/**
 * Class: com.elroid.wirelens.model.GoogleVisionResponse
 * Project: WireLens
 * Created Date: 18/01/2018 13:25
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class OcrResponse
{
	public enum Type
	{GOOGLE_VISION,TESSERACT,TEST}
	private Type type;
	private String text;
	private String[] lines;

	public OcrResponse(String text){
		this.text = text;
		this.type = Type.TEST;
	}

	public OcrResponse(String text, Type type){
		this.text = text;
		this.type = type;
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

	public Type getType(){
		return type;
	}
}

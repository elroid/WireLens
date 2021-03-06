package com.elroid.wirelens.model;

import com.elroid.wirelens.util.GenUtils;

/**
 * Class: com.elroid.wirelens.model.TextParserResponse
 * Project: WireLens
 * Created Date: 23/01/2018 15:09
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class TextParserResponse
{
	private String originalText;
	private String ssid;
	private String password;

	public TextParserResponse(String originalText){
		this.originalText = originalText;
	}

	public TextParserResponse(String originalText, String ssid, String password){
		this.originalText = originalText;
		this.ssid = ssid;
		this.password = password;
	}

	public TextParserResponse(String ssid, String password){
		this.ssid = ssid;
		this.password = password;
	}

	public String getOriginalText(){
		return originalText;
	}

	public String getSsid(){
		return ssid;
	}

	public void setSsid(String ssid){
		this.ssid = ssid;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public boolean hasSsid(){
		return !GenUtils.isBlank(ssid);
	}

	public boolean hasPassword(){
		return !GenUtils.isBlank(password);
	}

	public boolean hasBoth(){
		return hasSsid() && hasPassword();
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof TextParserResponse)) return false;

		TextParserResponse that = (TextParserResponse) o;

		if(ssid != null ? !ssid.equals(that.ssid) : that.ssid != null) return false;
		return password != null ? password.equals(that.password) : that.password == null;
	}

	@Override
	public int hashCode(){
		int result = ssid != null ? ssid.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder("TextParserResponse{");
		sb.append("ssid='").append(ssid).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

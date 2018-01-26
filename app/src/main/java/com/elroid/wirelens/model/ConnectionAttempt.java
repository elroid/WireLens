package com.elroid.wirelens.model;

/**
 * Class: com.elroid.wirelens.model.ConnectionAttempt
 * Project: WireLens
 * Created Date: 25/01/2018 17:46
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ConnectionAttempt
{
	private String ssid;
	private String password;

	public ConnectionAttempt(String ssid, String password){
		this.ssid = ssid;
		this.password = password;
	}

	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder("ConnectionAttempt{");
		sb.append("ssid='").append(ssid).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof ConnectionAttempt)) return false;

		ConnectionAttempt that = (ConnectionAttempt) o;

		if(ssid != null ? !ssid.equals(that.ssid) : that.ssid != null) return false;
		return password != null ? password.equals(that.password) : that.password == null;
	}

	@Override
	public int hashCode(){
		int result = ssid != null ? ssid.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}

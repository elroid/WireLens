package com.elroid.wirelens.model;

/**
 * Class: com.elroid.wirelens.model.WifiState
 * Project: WireLens
 * Created Date: 06/02/2018 15:28
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class WifiState
{
	public static final int UNKNOWN 	= 0;
	public static final int SAVED 		= 1;
	public static final int CONNECTING 	= 2;
	public static final int CONNECTED 	= 3;
	public static final int AUTH_ERROR 	= 4;

	private int state;

	public WifiState(int state){
		this.state = state;
	}

	public int getState(){
		return state;
	}
}

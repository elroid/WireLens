package com.elroid.wirelens.util;

import android.os.Looper;

/**
 * Class: com.elroid.wirelens.util.GenUtils
 * Project: WireLens
 * Created Date: 24/01/2018 14:05
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class GenUtils
{
	public static boolean isBlank(Object obj){
		if(obj == null) return true;
		else if(obj instanceof Object[]){
			Object[] arr = (Object[])obj;
			if(arr.length == 0) return true;
			else{
				for(Object o : arr){
					if(!isBlank(o)) return false;
				}
				return true;
			}
		}
		else
			return obj.toString().trim().equals("");
	}

	public static boolean isUIThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	public static int getWidthAtHeight(int w, int h, int newH) {
		return newH * w / h;
	}

	public static int getHeightAtWidth(int w, int h, int newW) {
		return newW * h / w;
	}
}

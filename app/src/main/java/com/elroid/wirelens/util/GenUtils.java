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
	public static boolean isUIThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
}

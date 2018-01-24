package com.elroid.wirelens.data.local.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * From: https://gist.github.com/Anrimian/f82269b53c1c372e650580dd2cd20ddb
 * Class: com.elroid.wirelens.data.local.wifi.BroadcastDisposable
 * Project: WireLens
 * Created Date: 24/01/2018 11:44
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class BroadcastDisposable implements Disposable
{

	private BroadcastReceiver receiver;
	private Context ctx;

	private boolean isDisposed = false;

	BroadcastDisposable(@NonNull BroadcastReceiver receiver, @NonNull Context ctx) {
		this.receiver = receiver;
		this.ctx = ctx;
	}

	@Override
	public void dispose() {
		if (!isDisposed) {
			ctx.unregisterReceiver(receiver);
			isDisposed = true;
		}
	}

	@Override
	public boolean isDisposed() {
		return isDisposed;
	}
}

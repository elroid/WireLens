package com.elroid.wirelens.data.local.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * From: https://gist.github.com/Anrimian/f82269b53c1c372e650580dd2cd20ddb
 * Class: com.elroid.wirelens.data.local.wifi.RxReceivers
 * Project: WireLens
 * Created Date: 24/01/2018 11:45
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class RxReceivers
{
	public static Observable<Intent> from(@NonNull final String action, @NonNull final Context ctx) {
		IntentFilter filter = new IntentFilter(action);
		return from(filter, ctx);
	}

	public static Observable<Intent> from(@NonNull final IntentFilter intentFilter, @NonNull final Context ctx) {
		return Observable.create(new ObservableOnSubscribe<Intent>() {
			Context appContext = ctx.getApplicationContext();
			@Override
			public void subscribe(@NonNull final ObservableEmitter<Intent> emitter) throws Exception {
				BroadcastReceiver receiver = new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						emitter.onNext(intent);
					}
				};
				emitter.setDisposable(new BroadcastDisposable(receiver, appContext));
				appContext.registerReceiver(receiver, intentFilter);
			}
		});
	}
}

package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.ConnectionAttempt;
import com.elroid.wirelens.model.CredentialsImage;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.domain.ConnectionManager
 * Project: WireLens
 * Created Date: 25/01/2018 17:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ConnectionManager
{
	private final WifiDataManager wifiManager;
	private final ConnectionGuesser guesser;

	@Inject
	public ConnectionManager(WifiDataManager wifiManager, ConnectionGuesser guesser){
		this.wifiManager = wifiManager;
		this.guesser = guesser;
	}

	public Single<Boolean> connect(CredentialsImage image){
		Timber.d("connect(image:%s)", image);

		return Single.create(emitter -> guesser.guess(image)
			.subscribe(new DisposableObserver<ConnectionAttempt>()
			{
				@Override
				public void onNext(ConnectionAttempt ca){
					Timber.i("attempting connect: %s", ca);
					wifiManager.connect(ca.getSsid(), ca.getPassword())
						.subscribe(() -> {
							Timber.i("connection successful!");
							emitter.onSuccess(true);
							dispose();
						}, e -> Timber.w(e, "connect was unsuccessful"));
				}

				@Override
				public void onError(Throwable e){
					Timber.w(e, "Error connecting!");
					emitter.onError(e);
				}

				@Override
				public void onComplete(){
					Timber.i("connection attempts complete");
					if(!emitter.isDisposed())
						emitter.onSuccess(false);
				}
			}));
	}
}

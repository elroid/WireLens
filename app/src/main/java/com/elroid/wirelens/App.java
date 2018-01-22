package com.elroid.wirelens;

import android.app.Activity;
import android.app.Application;

import com.elroid.wirelens.injection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.App
 * Project: WireLens
 * Created Date: 17/01/2018 17:27
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class App extends Application implements HasActivityInjector
{
	@Inject
	DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

	@Override
	public void onCreate(){
		super.onCreate();

		if(BuildConfig.DEBUG){
			Timber.plant(new Timber.DebugTree());
		}

		DaggerAppComponent
				.builder()
				.application(this)
				.build()
				.inject(this);
	}

	@Override
	public AndroidInjector<Activity> activityInjector(){
		return dispatchingAndroidInjector;
	}
}

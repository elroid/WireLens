package com.elroid.wirelens;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

import com.elroid.wirelens.injection.DaggerAppComponent;

import javax.inject.Inject;

import androidx.multidex.MultiDex;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.App
 * Project: WireLens
 * Created Date: 17/01/2018 17:27
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class App extends Application implements HasActivityInjector, HasServiceInjector
{
	@Inject
	DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

	@Inject
	DispatchingAndroidInjector<Service> dispatchingServiceInjector;

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

	@Override
	public AndroidInjector<Service> serviceInjector(){
		return dispatchingServiceInjector;
	}

	@Override protected void attachBaseContext(Context base){
		super.attachBaseContext(base);
		if(BuildConfig.DEBUG){
			MultiDex.install(this);
		}
	}
}

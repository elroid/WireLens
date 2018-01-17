package com.elroid.wirelens.injection;

import com.elroid.wirelens.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Class: com.elroid.wirelens.injection.AppComponent
 * Project: WireLens
 * Created Date: 17/01/2018 16:52
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Singleton
@Component(modules = {
		AndroidSupportInjectionModule.class,
		AppModule.class,
		BuildersModule.class})
public interface AppComponent
{
	@Component.Builder
	interface Builder
	{
		@BindsInstance
		Builder application(App application);

		AppComponent build();
	}

	void inject(App app);
}

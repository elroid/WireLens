package com.elroid.wirelens.injection;

import android.content.Context;

import com.elroid.wirelens.App;
import com.elroid.wirelens.data.remote.GoogleVisionClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Class: com.elroid.wirelens.injection.AppModule
 * Project: WireLens
 * Created Date: 17/01/2018 16:53
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Module
public class AppModule {

	@Provides
	Context provideContext(App application) {
		return application.getApplicationContext();
	}

	@Provides
	@Singleton
	GoogleVisionClient provideGoogleVisionClient(Retrofit.Builder builder){

		//create an api with okhttp cache
		Retrofit retrofit = builder
				.baseUrl(GoogleVisionClient.ENDPOINT)
				.build();
		return retrofit.create(GoogleVisionClient.class);
	}

	/*@Singleton
	@Provides
	CommonGreetingRepository provideCommonHelloService() {
		return new CommonGreetingRepository();
	}*/
}

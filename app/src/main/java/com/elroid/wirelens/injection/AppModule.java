package com.elroid.wirelens.injection;

import android.content.Context;

import com.elroid.wirelens.App;
import com.elroid.wirelens.data.local.GoogleVisionDataStore;
import com.elroid.wirelens.data.remote.GoogleVisionServiceClient;
import com.elroid.wirelens.domain.DataManager;
import com.elroid.wirelens.domain.GoogleVisionLocalRepository;
import com.elroid.wirelens.domain.GoogleVisionRemoteRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Class: com.elroid.wirelens.injection.AppModule
 * Project: WireLens
 * Created Date: 17/01/2018 16:53
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Module
public class AppModule
{

	@Provides
	Context provideContext(App application){
		return application.getApplicationContext();
	}

	@Provides
	@Singleton
	GoogleVisionLocalRepository provideGoogleVisionLocal(){
		return new GoogleVisionDataStore();
	}

	@Provides
	@Singleton
	GoogleVisionRemoteRepository provideGoogleVisionRemote(){
		return new GoogleVisionServiceClient();
	}

	@Provides
	@Singleton
	DataManager provideDataManager(GoogleVisionLocalRepository gvLocal, GoogleVisionRemoteRepository gvRemote){
		return new DataManager(gvLocal, gvRemote);
	}

	@Provides
	@Singleton
	Retrofit.Builder provideRestAdapterBuilder(OkHttpClient client){
		return new Retrofit.Builder()
				//.addConverterFactory(new NullOnEmptyConverterFactory())
				//.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.client(client);
	}

	@Provides
	@Singleton
	OkHttpClient provideOkClientBuilder(){

		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		builder.connectTimeout(30, TimeUnit.SECONDS);
		builder.readTimeout(30, TimeUnit.SECONDS);
		builder.writeTimeout(30, TimeUnit.SECONDS);

		//		Cache cache = new Cache(cacheDir, 1024 * 1024 * 10);
		//		builder.cache(cache);

		//		builder.addInterceptor(interceptor); //needed for force network
		//		builder.addNetworkInterceptor(interceptor); //needed for offline mode

		return builder.build();
	}

}

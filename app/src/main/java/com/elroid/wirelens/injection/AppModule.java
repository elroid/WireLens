package com.elroid.wirelens.injection;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.elroid.wirelens.App;
import com.elroid.wirelens.data.local.GoogleVisionDataStore;
import com.elroid.wirelens.data.local.SimpleTextParser;
import com.elroid.wirelens.data.local.wifi.WifiHelper;
import com.elroid.wirelens.data.remote.GoogleVisionServiceClient;
import com.elroid.wirelens.domain.ConnectionGuesser;
import com.elroid.wirelens.domain.ConnectionManager;
import com.elroid.wirelens.domain.DataManager;
import com.elroid.wirelens.domain.GoogleVisionLocalRepository;
import com.elroid.wirelens.domain.GoogleVisionRemoteRepository;
import com.elroid.wirelens.domain.ResourceProvider;
import com.elroid.wirelens.domain.TextParser;
import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.util.ResourceProviderImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

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
	GoogleVisionRemoteRepository provideGoogleVisionRemote(Context ctx){
		return new GoogleVisionServiceClient(ctx);
	}

	@Provides
	@Singleton
	DataManager provideDataManager(GoogleVisionLocalRepository gvLocal, GoogleVisionRemoteRepository gvRemote){
		return new DataManager(gvLocal, gvRemote);
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

	@Provides
	@Singleton
	WifiManager provideWifiManager(Context ctx){
		return (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
	}

	@Provides
	@Singleton
	WifiDataManager provideWifiDataManager(WifiManager wifiManager, Context ctx){
		return new WifiHelper(wifiManager, ctx);
	}

	@Provides
	@Singleton
	TextParser provideTextParser(){
		return new SimpleTextParser();
	}

	@Provides
	@Singleton
	ConnectionGuesser provideConnectionGuesser(DataManager dataManager,
											   TextParser textParser,
											   WifiDataManager wifiManager){
		return new ConnectionGuesser(dataManager, textParser, wifiManager);
	}

	@Provides
	@Singleton
	ConnectionManager provideConnectionManager(WifiDataManager wifiManager,
											   ConnectionGuesser guesser){
		return new ConnectionManager(wifiManager, guesser);
	}

	@Provides
	@Singleton
	ResourceProvider provideResourceProvider(Context ctx){
		return new ResourceProviderImpl(ctx);
	}
}

package com.elroid.wirelens.data.local;

import com.elroid.wirelens.domain.GoogleVisionLocalRepository;
import com.elroid.wirelens.model.GoogleVisionResponse;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.data.local.GoogleVisionDataStore
 * Project: WireLens
 * Created Date: 18/01/2018 13:23
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class GoogleVisionDataStore implements GoogleVisionLocalRepository
{
	@Override public Completable saveVisionResponse(File imgFile, GoogleVisionResponse response){
		return Completable.complete();//todo
	}

	@Override public Single<GoogleVisionResponse> getVisionResponse(File imgFile){
		//return Single.just(new GoogleVisionResponse("Your message here"));//todo
		return Single.error(new Exception("Data not cached"));
	}
}

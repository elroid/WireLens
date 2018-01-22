package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.GoogleVisionResponse;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.domain.DataManager
 * Project: WireLens
 * Created Date: 18/01/2018 13:55
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Singleton
public class DataManager
{
	private final GoogleVisionLocalRepository localVisionRespository;
	private final GoogleVisionRemoteRepository remoteVisionRepository;

	@Inject
	public DataManager(GoogleVisionLocalRepository localVisionRespository,
										 GoogleVisionRemoteRepository remoteVisionRepository){
		this.localVisionRespository = localVisionRespository;
		this.remoteVisionRepository = remoteVisionRepository;
	}

	public Single<GoogleVisionResponse> extractText(File imgFile){
		return Single.error(new Exception("Not yet implemented"));
	}

	public Single<GoogleVisionResponse> retrieveRemoteGoogleVisionResponse(File imgFile){
		return remoteVisionRepository.getVisionResponse(imgFile);
	}

	public Single<GoogleVisionResponse> retrieveLocalGoogleVisionResponse(File imgFile){
		return localVisionRespository.getVisionResponse(imgFile);
	}
}

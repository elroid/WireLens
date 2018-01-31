package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.WifiNetwork;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.domain.WifiDataManager
 * Project: WireLens
 * Created Date: 24/01/2018 10:27
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public interface WifiDataManager
{
	Observable<List<WifiNetwork>> scan();
	Completable connect(String ssid, String password);
}

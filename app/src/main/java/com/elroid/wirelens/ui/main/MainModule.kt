package com.elroid.wirelens.ui.main

import android.net.wifi.WifiManager
import com.elroid.wirelens.domain.ResourceProvider
import com.elroid.wirelens.util.FileUtils
import dagger.Module
import dagger.Provides

/**
 * Class: com.elroid.wirelens.ui.main.MainModule
 * Project: WireLens
 * Created Date: 06/02/2018 16:04
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Module
class MainModule {
	@Provides
	internal fun provideMainPresenter(view:MainContract.View,
									  resourceProvider:ResourceProvider,
									  fileUtils:FileUtils,
									  wifiManager:WifiManager):MainPresenter {
		return MainPresenter(view, resourceProvider, fileUtils, wifiManager)
	}

}

package com.elroid.wirelens

import android.app.Application
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import timber.log.Timber

/**
 *
 * Class: .com.elroid.wirelens.App
 * Project: WireLens
 * Created Date: 18/12/2017 17:15
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2017 Elroid Ltd. All rights reserved.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if ("debug" == BuildConfig.BUILD_TYPE) {
            //send log messages to logcat
            Timber.plant(Timber.DebugTree())

        }

        // Create global configuration and initialize ImageLoader with this config
        val config = ImageLoaderConfiguration.Builder(this)
                //...
                .build()
        ImageLoader.getInstance().init(config)
    }
}
package com.elroid.wirelens

import android.app.Application
import android.content.Context
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File

/**
 * Base class for Robolectric data layer tests.
 * Inherit from this class to create a test.
 *
 * Class: com.elroid.wirelens.test.RoboelectricTest
 * Project: WireLens
 * Created Date: 22/01/2018 16:23
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
    application = RoboelectricTest.ApplicationStub::class,
    sdk = intArrayOf(21))
abstract class RoboelectricTest {

  fun getCtx(): Context {
    return RuntimeEnvironment.application
  }

  fun cacheDir(): File {
    return getCtx().cacheDir
  }

  internal class ApplicationStub : Application()
}

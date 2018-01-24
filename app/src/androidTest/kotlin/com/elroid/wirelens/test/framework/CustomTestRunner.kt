package com.elroid.wirelens.test.framework

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.support.test.runner.AndroidJUnitRunner
import android.util.Log


/**
 * Tests can fail for other reasons than code, it´ because of the animations and espresso sync and
 * emulator state (screen off or locked)
 * <p/>
 * Before all the tests prepare the device to run tests and avoid these problems.
 * <p/>
 * - Disable animations
 * - Disable keyguard lock
 * - Set it to be awake all the time (dont let the processor sleep)
 *
 * @see <a href="u2020 open source app by Jake Wharton">https://github.com/JakeWharton/u2020</a>
 * @see <a href="Daj gist">https://gist.github.com/daj/7b48f1b8a92abf960e7b</a>
 *
 *
 * Class: com.elroid.wirelens.test.framework.UnlockDeviceAndroidJUnitRunner
 * Project: WireLens
 * Created Date: 24/01/2018 15:06
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class CustomTestRunner:AndroidJUnitRunner() {

	private val TAG = "CustomTestRunner"

	override fun onStart() {

		runOnMainSync {
			val app = this@CustomTestRunner.getTargetContext().getApplicationContext()

			this@CustomTestRunner.disableAnimations(app)

			val name = CustomTestRunner::class.java!!.getSimpleName()
			unlockScreen(app, name)
			keepSceenAwake(app, name)
		}

		super.onStart()
	}


	override fun finish(resultCode: Int, results: Bundle) {
		super.finish(resultCode, results)
		enableAnimations(context)
	}

	private fun keepSceenAwake(app: Context, name: String) {
		val power = app.getSystemService(Context.POWER_SERVICE) as PowerManager
		power.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, name)
			.acquire()
	}

	private fun unlockScreen(app: Context, name: String) {
		val keyguard = app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
		keyguard.newKeyguardLock(name).disableKeyguard()
	}

	fun disableAnimations(context: Context) {
		val permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE)
		if (permStatus == PackageManager.PERMISSION_GRANTED) {
			setSystemAnimationsScale(0.0f)
		}
	}

	fun enableAnimations(context: Context) {
		val permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE)
		if (permStatus == PackageManager.PERMISSION_GRANTED) {
			setSystemAnimationsScale(1.0f)
		}
	}

	private fun setSystemAnimationsScale(animationScale: Float) {
		try {
			val windowManagerStubClazz = Class.forName("android.view.IWindowManager\$Stub")
			val asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder::class.java)
			val serviceManagerClazz = Class.forName("android.os.ServiceManager")
			val getService = serviceManagerClazz.getDeclaredMethod("getService", String::class.java)
			val windowManagerClazz = Class.forName("android.view.IWindowManager")
			val setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", FloatArray::class.java)
			val getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales")

			val windowManagerBinder = getService.invoke(null, "window") as IBinder
			val windowManagerObj = asInterface.invoke(null, windowManagerBinder)
			val currentScales = getAnimationScales.invoke(windowManagerObj) as FloatArray
			for (i in currentScales.indices) {
				currentScales[i] = animationScale
			}
			setAnimationScales.invoke(windowManagerObj, arrayOf<Any>(currentScales))
			Log.d(TAG, "Changed permissions of animations")
		} catch (e: Exception) {
			Log.e(TAG, "Could not change animation scale to $animationScale :'(", e)
		}

	}
}
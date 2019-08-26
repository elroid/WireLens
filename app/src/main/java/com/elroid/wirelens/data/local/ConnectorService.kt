package com.elroid.wirelens.data.local

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator

import com.elroid.wirelens.R
import com.elroid.wirelens.domain.ConnectionManager
import com.elroid.wirelens.domain.WifiDataManager
import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.ui.base.SchedulersFacade
import com.elroid.wirelens.ui.manual.ManualConnectActivity

import java.io.File
import java.io.IOException

import javax.inject.Inject
import androidx.core.content.ContextCompat
import com.github.ajalt.timberkt.v
import com.github.ajalt.timberkt.i
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.w
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Class: com.elroid.wirelens.data.local.ConnectorService
 * Project: WireLens
 * Created Date: 26/01/2018 16:46
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class ConnectorService:Service() {

	@Inject internal lateinit var connManager:ConnectionManager
	@Inject internal lateinit var schedulers:SchedulersFacade
	@Inject internal lateinit var wifiManager:WifiDataManager

	override fun onCreate() {
		AndroidInjection.inject(this)
		super.onCreate()
	}

	override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
		Timber.d("onStartCommand(intent:%s, flags:%s, startId:%s)", intent, flags, startId)

		startForeground(ONGOING_NOTIFICATION_ID, createNotification())
		try {
			val filePath = intent.getStringExtra(FILE) ?: throw IOException(
				"File path was not specified")
			val file = File(filePath)

			add(connManager.connect(CredentialsImage(file))
				.subscribeOn(schedulers.io())
				.observeOn(schedulers.io())
				.subscribe({ success ->
					d{"done with connection, success:$success"}
					done(success!!)
				}, { ex ->
					w(ex){"Error connecting"}
					done(false)
				}))

			Timber.d("STARTING STICKY")
			return START_STICKY
		} catch(e:IOException) {
			Timber.w(e, "Error instantiating connection service")

			Timber.d("STARTING NOT_STICKY")
			return START_NOT_STICKY
		}

	}

	private fun done(success:Boolean) {
		vibrateResult(success)
		stopSelf()
	}

	private fun createNotification():Notification {
		val notificationIntent = ManualConnectActivity.createIntent(this)
		val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

		return Notification.Builder(this)//todo notification channel!
			.setContentTitle(getText(R.string.notification_title))
			.setContentText(getText(R.string.notification_message))
			.setSmallIcon(R.drawable.ic_notification)
			.setContentIntent(pendingIntent)
			.setTicker(getText(R.string.notification_title))
			.setPriority(Notification.PRIORITY_HIGH)
			.build()
	}

	private fun vibrateResult(success:Boolean) {
		try {
			val pattern:LongArray = if(success)
				longArrayOf(0, 100, 100, 100, 100, 500)//ta ta daaaaaaa
			else
				longArrayOf(0, 1000, 100, 1000)//ooooooh noooooo
			vibrate(pattern)

		} catch(e:Exception) {
			Timber.w(e, "Unable to vibrate")
		}
	}

	private fun vibrate(pattern:LongArray, repeat:Int = -1){
		val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
		if(vibrator.hasVibrator()) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat))
			}
			else {
				@Suppress("DEPRECATION")
				vibrator.vibrate(pattern, repeat)
			}
		} else {
			Timber.w("no vibrator available!")
		}
	}

	override fun onBind(intent:Intent):IBinder? {
		Timber.d("onBind(intent:%s)", intent)
		return null
	}

	override fun onDestroy() {
		Timber.i("service destroyed")
		disposables.clear()
		super.onDestroy()
	}

	companion object {
		private val ONGOING_NOTIFICATION_ID = 123

		private val FILE = "File"

		@Throws(IOException::class)
		fun createIntent(ctx:Context, image:CredentialsImage):Intent {
			val i = Intent(ctx, ConnectorService::class.java)
			i.putExtra(FILE, image.getFile(ctx).absolutePath)
			return i
		}

		@Throws(IOException::class)
		fun start(ctx:Context, image:CredentialsImage) {
			val i = createIntent(ctx, image)
			ContextCompat.startForegroundService(ctx, i)
		}
	}

	private val disposables = CompositeDisposable()
	private fun add(disposable:Disposable) {
		disposables.add(disposable)
	}
}

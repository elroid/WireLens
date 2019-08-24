package com.elroid.wirelens.data.local;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

import com.elroid.wirelens.R;
import com.elroid.wirelens.domain.ConnectionManager;
import com.elroid.wirelens.domain.WifiDataManager;
import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.ui.base.SchedulersFacade;
import com.elroid.wirelens.ui.manual.ManualConnectActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.data.local.ConnectorService
 * Project: WireLens
 * Created Date: 26/01/2018 16:46
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ConnectorService extends Service
{
	private static final int ONGOING_NOTIFICATION_ID = 123;

	@Inject ConnectionManager connManager;
	@Inject SchedulersFacade schedulers;
	@Inject WifiDataManager wifiManager;

	private static final String FILE = "File";

	public static Intent createIntent(Context ctx, CredentialsImage image) throws IOException{
		Intent i = new Intent(ctx, ConnectorService.class);
		i.putExtra(FILE, image.getFile(ctx).getAbsolutePath());
		return i;
	}

	public static void start(Context ctx, CredentialsImage image) throws IOException{
		Intent i = createIntent(ctx, image);
		ContextCompat.startForegroundService(ctx, i);
	}

	@Override
	public void onCreate(){
		AndroidInjection.inject(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Timber.d("onStartCommand(intent:%s, flags:%s, startId:%s)", intent, flags, startId);

		startForeground(ONGOING_NOTIFICATION_ID, createNotification());
		try{
			String filePath = intent.getStringExtra(FILE);
			if(filePath == null)
				throw new IOException("File path was not specified");
			File file = new File(filePath);

			connManager.connect(new CredentialsImage(file))
				.subscribeOn(schedulers.io())
				.observeOn(schedulers.io())
				.subscribe(success -> {
					Timber.d("done with connection, success:%s", success);
					done(success);
				}, e -> {
					Timber.w(e, "Error connecting");
					done(false);
				});

			Timber.d("STARTING STICKY");
			return START_STICKY;
		}
		catch(IOException e){
			Timber.w(e, "Error instantiating connection service");

			Timber.d("STARTING NOT_STICKY");
			return START_NOT_STICKY;
		}
	}

	private void done(boolean success){
		vibrateResult(success);
		stopSelf();
	}

	private Notification createNotification(){
		Intent notificationIntent = ManualConnectActivity.createIntent(this);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		return new Notification.Builder(this)//todo notification channel!
			.setContentTitle(getText(R.string.notification_title))
			.setContentText(getText(R.string.notification_message))
			.setSmallIcon(R.drawable.ic_notification)
			.setContentIntent(pendingIntent)
			.setTicker(getText(R.string.notification_title))
			.setPriority(Notification.PRIORITY_HIGH)
			.build();
	}

	private void vibrateResult(boolean success){
		try{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			if(vibrator != null && vibrator.hasVibrator()){
				long[] pattern;
				if(success)
					pattern = new long[]{0, 100, 100, 100, 100, 500};//ta ta daaaaaaa
				else
					pattern = new long[]{0, 1000, 100, 1000};//ooooooh noooooo
				vibrator.vibrate(pattern, -1);
			}
			else{
				Timber.w("no vibrator available!");
			}
		}
		catch(Exception e){
			Timber.w(e, "Unable to vibrate");
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent){
		Timber.d("onBind(intent:%s)", intent);
		return null;
	}

	@Override
	public void onDestroy(){
		Timber.i("service destroyed");
		super.onDestroy();
	}
}

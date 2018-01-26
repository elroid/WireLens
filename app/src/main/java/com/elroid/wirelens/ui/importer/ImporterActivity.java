package com.elroid.wirelens.ui.importer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.elroid.wirelens.R;
import com.elroid.wirelens.domain.DataManager;
import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.ui.base.BaseActivity;
import com.elroid.wirelens.ui.base.SchedulersFacade;
import com.elroid.wirelens.util.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.ui.importer.ImporterActivity
 * Project: WireLens
 * Created Date: 17/01/2018 18:19
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ImporterActivity extends BaseActivity
{
	public static Intent createIntent(Context ctx){
		return new Intent(ctx, ImporterActivity.class);
	}

	@Inject DataManager dataManager;
	@Inject SchedulersFacade schedulers;

	TextView hello;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		hello = findViewById(R.id.hello);
		Intent intent = getIntent();
		if(intent != null){
			Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
			Timber.d("Got imageUri: %s", imageUri);
			if(imageUri != null){
				try{
					File file = FileUtils.pickedExistingPicture(this, imageUri);
					Timber.d("Got file: %s", file);

					hello.setText("Analysing...");
					dataManager.extractText(new CredentialsImage(file))
						.subscribeOn(schedulers.io())
						.observeOn(schedulers.ui())
						.subscribe(myData -> {
							Timber.d("Got my data: %s", myData.getText());
							hello.setText(myData.getText());
						}, e -> {
							Timber.w(e, "Error getting vision ocr data");
						});

				}
				catch(IOException e){
					Timber.w(e, "Error getting image file");
				}
			}
			else toast("no imageUri!");
		}
		else toast("no intent!");
	}


}
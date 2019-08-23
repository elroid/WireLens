package com.elroid.wirelens.ui.base;

import android.content.Context;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.ui.base.BaseActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:46
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public abstract class BaseActivity extends AppCompatActivity
{
	protected void toast(String str){
		Timber.d("toast: %s", str);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	protected Context getCtx(){
		return this;
	}
}

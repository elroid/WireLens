package com.elroid.wirelens.ui.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elroid.wirelens.ui.base.BaseActivity;
import com.elroid.wirelens.ui.main.MainActivity;
import com.elroid.wirelens.ui.test.TestActivity;
import com.elroid.wirelens.ui.test.TestKotlinActivity;

/**
 * Class: com.elroid.wirelens.ui.start.StartActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:46
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class StartActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		startActivity(MainActivity.create.intent(this));
//		startActivity(TestActivity.createIntent(this));
//		startActivity(TestKotlinActivity.create.intent(this));

		finish();
	}
}

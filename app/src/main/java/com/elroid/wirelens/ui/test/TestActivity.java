package com.elroid.wirelens.ui.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elroid.wirelens.R;
import com.elroid.wirelens.ui.base.BaseActivity;

/**
 * Class: com.elroid.wirelens.ui.test.TestActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:51
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class TestActivity extends BaseActivity
{
	public static Intent createIntent(Context ctx){
		return new Intent(ctx, TestActivity.class);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_java);
	}
}

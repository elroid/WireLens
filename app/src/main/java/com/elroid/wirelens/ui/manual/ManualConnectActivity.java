package com.elroid.wirelens.ui.manual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.elroid.wirelens.ui.base.BaseActivity;

/**
 * Class: com.elroid.wirelens.ui.manual.ManualConnectActivity
 * Project: WireLens
 * Created Date: 26/01/2018 17:11
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class ManualConnectActivity extends BaseActivity
{
	public static Intent createIntent(Context ctx){
		return new Intent(ctx, ManualConnectActivity.class);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState){
		super.onCreate(savedInstanceState, persistentState);
		TextView txt = new TextView(this);
		txt.setText("Manual connect activity (to-do)");
		setContentView(txt);
	}
}

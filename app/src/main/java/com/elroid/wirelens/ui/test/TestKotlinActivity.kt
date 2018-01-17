package com.elroid.wirelens.ui.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.elroid.wirelens.R
import com.elroid.wirelens.ui.base.BaseActivity
import kotlinx.android.synthetic.main.test_kotlin.*

/**
 *
 * Class: com.elroid.wirelens.ui.test.TestKotlinActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:54
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class TestKotlinActivity : BaseActivity() {

    companion object create {
        fun intent(ctx: Context): Intent {
            return Intent(ctx, TestKotlinActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_kotlin)

        narf.text = "Kotlin rocks!"
    }
}
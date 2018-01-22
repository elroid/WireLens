package com.elroid.wirelens.uitest

import com.elroid.wirelens.R
import com.elroid.wirelens.ui.main.MainActivity
import com.elroid.wirelens.ui.start.StartActivity
import com.elroid.wirelens.uitest.framework.AcceptanceTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MainActivityTest : AcceptanceTest<StartActivity>(StartActivity::class.java) {

  @Test
  fun shouldDisplayAction() {
    checkThat.viewIsVisible(R.id.fab)
  }
}

package com.elroid.wirelens.uitest

import com.elroid.wirelens.R
import com.elroid.wirelens.ui.main.MainActivity
import com.elroid.wirelens.ui.start.StartActivity
import com.elroid.wirelens.uitest.framework.AcceptanceTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MainActivityTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

  @Test
  fun checkFabIsVisivle() {
    checkThat.viewIsVisible(R.id.fab)
  }
}

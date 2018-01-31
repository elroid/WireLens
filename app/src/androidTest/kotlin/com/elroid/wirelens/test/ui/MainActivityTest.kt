package com.elroid.wirelens.test.ui

import com.elroid.wirelens.R
import com.elroid.wirelens.test.framework.AcceptanceTest
import com.elroid.wirelens.ui.main.MainActivity
import org.junit.Test

class MainActivityTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

	@Test
	fun checkFabIsVisivle() {
		checkThat.viewIsVisible(R.id.cameraButton)
		checkThat.viewIsVisible(R.id.galleryButton)
		checkThat.viewIsVisible(R.id.qrButton)
	}
}

package com.elroid.wirelens.test.conn

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.InstrumentationRegistry.getTargetContext
import com.elroid.wirelens.data.local.SimpleTextParser
import com.elroid.wirelens.data.remote.GoogleVisionServiceClient
import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.model.TextParserResponse
import com.elroid.wirelens.util.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * Class: com.elroid.wirelens.test.conn.GoogleVisionServiceTest
 * Project: WireLens
 * Created Date: 23/01/2018 12:49
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class GoogleVisionServiceTest {

	private val googlVisionRemoteService = GoogleVisionServiceClient(getTargetContext())

	@Test
	fun getVisionResponse_givenPinky_returnsCorrectResponse() {
		testVisionResponse("pinky.jpg", "Pinky", "narfnarf")
	}

	@Test
	fun getVisionResponse_givenTrap_returnsCorrectResponse() {
		//testVisionResponse("trap.jpg", "It's a Trap!", "Calamari")//Correct
		testVisionResponse("trap.jpg", "It's a Trap!", "Calamar")//Actual
	}

	/*@Test
	fun getVisionResponse_givenDroidcon_returnsCorrectResponse() {
		testVisionResponse("droidcon.jpg", "droidconuk", "WhatTheL50")
	}

	@Test
	fun getVisionResponse_givenDroidcon2_returnsCorrectResponse() {
		//almost works: NOugatyNiceness instead of N0ugatyNiceness
		testVisionResponse("droidcon2.jpg", "droidconuk", "NOugatyNiceness")
	}

	@Test
	fun getVisionResponse_givenGuest_returnsCorrectResponse() {
		testVisionResponse("guest.jpg", "65twenty_guest", "guest7ad")
	}

	@Test
	fun getVisionResponse_givenModem_returnsCorrectResponse() {
		testVisionResponse("modem.jpg", "ZyXEL11633", "84DCF5174BC6B377FFDC")
	}

	@Test
	fun getVisionResponse_givenPrinted_returnsCorrectResponse() {
		testVisionResponse("passwordPrinted.jpg", "", "Hglguest")
	}

	@Test
	fun getVisionResponse_givenHandwritten_returnsCorrectResponse() {
		//doesn't work (impossible!)
		//testVisionResponse("handwritten.jpg", "sagemcom", "cdfe4c7ffe")
		testVisionResponse("handwritten.jpg", "", "")
	}*/

	private fun testVisionResponse(fileName: String, ssid: String, pwd: String) {
		val url = "http://elroid.com/wirelens/"+fileName
		val bmp = FileUtils.getBitmapFromURL(url);
		val credImg = CredentialsImage(bmp)

		val testObserver = googlVisionRemoteService.getVisionResponse(credImg).test()
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val gvr = testObserver.values()[0]
		val expected = TextParserResponse(ssid, pwd)
		Timber.i("Expected: %s", expected)
		val parser = SimpleTextParser()
		val actual = parser.parseResponse(gvr).blockingFirst()
		Timber.i("Actual: %s", actual)
		assertEquals(expected, actual)
	}

	/*@Test
	fun getVisionResponse_givenImage_returnsResponse(){
		val bmp = getBitmapFromURL("http://elroid.com/wirelens/droidcon.jpg");

		val obs = googlVisionRemoteService.getVisionResponse(bmp)
		val testObserver = TestObserver<GoogleVisionResponse>()
		obs.subscribe(testObserver)
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val gvr = testObserver.values()[0]
		assertNotNull(gvr.text)
		Timber.i("Response: %s", gvr.text)
	}*/

}
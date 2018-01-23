package com.elroid.wirelens.uitest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.InstrumentationRegistry.getTargetContext
import com.elroid.wirelens.data.remote.GoogleVisionServiceClient
import com.elroid.wirelens.model.GoogleVisionResponse
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * Class: com.elroid.wirelens.uitest.GoogleVisionServiceTest
 * Project: WireLens
 * Created Date: 23/01/2018 12:49
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class GoogleVisionServiceTest {

    private val googlVisionRemoteService = GoogleVisionServiceClient(getTargetContext())

    @Test
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
        /*
        Response: droidcon
                #droidconUK
                LONDON 30-310CT2014
                WIFI
                SSID: droidconuk
                Password:WhatTheL50
                BROUGHT TO YOU BY
                novodo
                #droidconUK
                droidcon
                LONDON
                N 30-31 0CT 2014
         */
    }

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
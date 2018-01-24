package com.elroid.wirelens

import com.elroid.wirelens.data.local.SimpleTextParser
import com.elroid.wirelens.model.GoogleVisionResponse
import com.elroid.wirelens.model.TextParserResponse
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * Class: com.elroid.wirelens.TextParserTest
 * Project: WireLens
 * Created Date: 23/01/2018 15:39
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class TextParserTest {

	@Test
	fun getLines_givenNormalText_returnsLines() {
		val gvr = GoogleVisionResponse(
			"""
            |line1
            |line2
            |line3
            """.trimMargin()
		)
		val expected = arrayOf("line1", "line2", "line3")
		assertArrayEquals(expected, gvr.lines);
	}

	val parser = SimpleTextParser()

	@Test
	fun getSSID_givenTextWithOneSSID_returnsOneSSID() {
		val gvr = GoogleVisionResponse(
			"""
            |some:line1
            |ssid: line2
            |Else    line3
            """.trimMargin()
		)

		val obs = parser.getSSID(gvr)
		val testObserver = TestObserver<String>()
		obs.subscribe(testObserver)
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val result = testObserver.values().get(0)
		assertEquals("line2", result)
	}

	@Test
	fun getSSID_givenTextWithTwoSSIDs_returnsTwoSSIDs() {
		val gvr = GoogleVisionResponse(
			"""
            |some:line1
            |ssid: line2
            |wifi ssid    line3
            """.trimMargin()
		)

		val obs = parser.getSSID(gvr)
		val testObserver = TestObserver<String>()
		obs.subscribe(testObserver)
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 2)

		assertEquals("line2", testObserver.values().get(0))
		assertEquals("line3", testObserver.values().get(1))
	}

	@Test
	fun parseResponse_givenDroidconData_returnsCorrectCredentials() {
		val gvr = GoogleVisionResponse(
			"""
            |droidcon
            |#droidconUK
            |LONDON 30-310CT2014
            |WIFI
            |SSID: droidconuk
            |Password:WhatTheL50
            |BROUGHT TO YOU BY
            |novodo
            |#droidconUK
            |droidcon
            |LONDON
            |N 30-31 0CT 2014
            """.trimMargin()
		)
		val tpr: TextParserResponse = parser.parseResponse(gvr).blockingFirst()
		Assert.assertEquals("droidconuk", tpr.ssid)
		Assert.assertEquals("WhatTheL50", tpr.password)
	}

	@Test
	fun parseResponse_givenDroidcon2Data_returnsCorrectCredentials() {
		val gvr = GoogleVisionResponse(
			"""
			|droidcon
			|LONDON
			|wifi SSID: droidconuk
			|Password:
			|NOugatyNiceness
            """.trimMargin()
		)
		val tpr: TextParserResponse = parser.parseResponse(gvr).blockingFirst()
		Assert.assertEquals("droidconuk", tpr.ssid)
		Assert.assertEquals("NOugatyNiceness", tpr.password)
	}

}
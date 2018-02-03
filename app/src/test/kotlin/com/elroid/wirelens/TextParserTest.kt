package com.elroid.wirelens

import com.elroid.wirelens.data.local.SimpleTextParser
import com.elroid.wirelens.framework.RoboelectricTest
import com.elroid.wirelens.model.OcrResponse
import com.elroid.wirelens.model.TextParserResponse
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
class TextParserTest: RoboelectricTest() {

	@Test
	fun getLines_givenNormalText_returnsLines() {

		//given
		val gvr = OcrResponse(
			"""
            |line1
            |line2
            |line3
            """.trimMargin()
		)

		//when
		val actual = gvr.lines

		//then
		val expected = arrayOf("line1", "line2", "line3")
		assertArrayEquals(expected, actual);
	}

	val parser = SimpleTextParser()

	@Test
	fun getSSID_givenTextWithOneSSID_returnsOneSSID() {

		//given
		val gvr = OcrResponse(
			"""
            |some:line1
            |ssid: line2
            |Else    line3
            """.trimMargin()
		)

		//when
		val testObserver = parser.getSSID(gvr).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val result = testObserver.values().get(0)
		assertEquals("line2", result)
	}

	@Test
	fun getSSID_givenTextWithTwoSSIDs_returnsTwoSSIDs() {

		//given
		val gvr = OcrResponse(
			"""
            |some:line1
            |ssid: line2
            |wifi ssid    line3
            """.trimMargin()
		)

		//when
		val testObserver = parser.getSSID(gvr).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 2)
		assertEquals("line2", testObserver.values().get(0))
		assertEquals("line3", testObserver.values().get(1))
	}

	@Test
	fun parseResponse_givenDroidconData_returnsCorrectCredentials() {

		//given
		val gvr = OcrResponse(
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

		//when
		val tpr: TextParserResponse = parser.parseResponse(gvr).blockingFirst()

		//then
		assertEquals("droidconuk", tpr.ssid)
		assertEquals("WhatTheL50", tpr.password)
	}

	@Test
	fun parseResponse_givenDroidcon2Data_returnsCorrectCredentials() {

		//given
		val gvr = OcrResponse(
			"""
			|droidcon
			|LONDON
			|wifi SSID: droidconuk
			|Password:
			|NOugatyNiceness
            """.trimMargin()
		)

		//when
		val tpr: TextParserResponse = parser.parseResponse(gvr).blockingFirst()

		//then
		assertEquals("droidconuk", tpr.ssid)
		assertEquals("NOugatyNiceness", tpr.password)
	}

}
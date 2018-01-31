package com.elroid.wirelens.domain

import android.net.wifi.WifiManager
import com.elroid.wirelens.data.local.SimpleTextParser
import com.elroid.wirelens.model.ConnectionAttempt
import com.elroid.wirelens.model.GoogleVisionResponse
import com.elroid.wirelens.model.TextParserResponse
import com.elroid.wirelens.model.WifiNetwork
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals

/**
 *
 * Class: com.elroid.wirelens.domain.ConnectionManagerTest
 * Project: WireLens
 * Created Date: 26/01/2018 14:28
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class ConnectionManagerTest {
	val wifiNetworks = Observable.just(listOf(
		WifiNetwork("Strong Network", -10),
		WifiNetwork("Decent Network", -15),
		WifiNetwork("Normal Network", -20),
		WifiNetwork("Weak Network", -100)
	))
	val localVision : GoogleVisionLocalRepository = mock()
	val remoteVision : GoogleVisionRemoteRepository = mock()
	val textParser = SimpleTextParser()
	val dataManager = DataManager(localVision, remoteVision)
	val wifiManager = mock<WifiDataManager> {
		on { scan() }.doReturn(wifiNetworks)
	}
	val connectionManager = ConnectionManager(dataManager, textParser, wifiManager)

	@Test
	fun connect_givenExact_emitsSingleAttempt(){
		//given
		val tpro = Observable.just(TextParserResponse("Strong Network", "myPassword"))
		val expected = ConnectionAttempt("Strong Network", "myPassword")

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val actual = testObserver.values().get(0)
		assertEquals(expected, actual)
	}

	@Test
	fun connect_givenSsidInWrongCase_emitsSingleAttempt(){
		//given
		val tpro = Observable.just(TextParserResponse("STRONG Network", "myPassword"))
		val expected = ConnectionAttempt("Strong Network", "myPassword")

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val actual = testObserver.values().get(0)
		assertEquals(expected, actual)
	}

	@Test
	fun connect_givenSsidNotFound_emitsError(){
		//given
		val tpro = Observable.just(TextParserResponse("Some Other Network", "myPassword"))
		val expected = ConnectionAttempt("Some Other Network", "myPassword")

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertError(Exception::class.java)
		/*assertEquals(testObserver.valueCount(), 1)
		val actual = testObserver.values().get(0)
		assertEquals(expected, actual)*/
	}

	@Test
	fun connect_givenNoSsidParsed_emitsTopThreeSSIDAttempts(){
		//given
		val tpro = Observable.just(TextParserResponse(null, "myPassword"))
		val expected0 = ConnectionAttempt("Strong Network", "myPassword")
		val expected1 = ConnectionAttempt("Decent Network", "myPassword")
		val expected2 = ConnectionAttempt("Normal Network", "myPassword")

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(3, testObserver.valueCount())
		assertEquals(expected0, testObserver.values().get(0))
		assertEquals(expected1, testObserver.values().get(1))
		assertEquals(expected2, testObserver.values().get(2))
	}

	@Test
	fun connect_givenSsidParsedButNoPassword_emitsSingleOpenNetworkAttempt(){
		//given
		val tpro = Observable.just(TextParserResponse("Strong Network", ""))
		val expected = ConnectionAttempt("Strong Network", "")

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(testObserver.valueCount(), 1)
		val actual = testObserver.values().get(0)
		assertEquals(expected, actual)
	}

	@Test
	fun connect_givenNothingParsed_throwsError(){
		//given
		val tpro = Observable.just(TextParserResponse("", ""))

		//when
		val testObserver = connectionManager.connect(tpro).test()

		//then
		testObserver.assertError(Exception::class.java)
	}
}
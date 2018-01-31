package com.elroid.wirelens.domain

import com.elroid.wirelens.framework.RoboelectricTest
import com.elroid.wirelens.model.ConnectionAttempt
import com.elroid.wirelens.model.CredentialsImage
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test
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
class ConnectionManagerTest: RoboelectricTest() {

	val credImg: CredentialsImage = mock()
	val correctSSID = "Correct SSID"
	val correctPassword = "Correct Password"
	val incorrectPassword = "Incorrect Password"
	val wifiManager: WifiDataManager = mock {
		on { connect(correctSSID, correctPassword) }
			.thenReturn(Completable.complete())
			.thenReturn(Completable.error(Exception("Connected twice")))
		on { connect(correctSSID, incorrectPassword) }.thenReturn(Completable.error(Exception()))
	}

	@Test
	fun connect_givenSingleCorrect_completeTrue() {
		//given
		val guessObservable = Observable.just(ConnectionAttempt(correctSSID, correctPassword))
		val guesser = mock<ConnectionGuesser> {
			on { guess(credImg) }.thenReturn(guessObservable)
		}
		val connectionManager = ConnectionManager(wifiManager, guesser)

		//when
		val testObserver = connectionManager.connect(credImg).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(1, testObserver.valueCount())
		val actual = testObserver.values().get(0)
		assertEquals(true, actual)
	}

	@Test
	fun connect_givenSingleIncorrect_completeFalse() {
		//given
		val guessObservable = Observable.just(ConnectionAttempt(correctSSID, incorrectPassword))
		val guesser = mock<ConnectionGuesser> {
			on { guess(credImg) }.thenReturn(guessObservable)
		}
		val connectionManager = ConnectionManager(wifiManager, guesser)

		//when
		val testObserver = connectionManager.connect(credImg).test()

		//then
		testObserver.assertNoErrors()
		assertEquals(1, testObserver.valueCount())
		val actual = testObserver.values().get(0)
		assertEquals(false, actual)
	}

	@Test
	fun mock_multiReturn() {
		//given
		val wifiManager: WifiDataManager = mock {
			on { connect(correctSSID, correctPassword) }
				.thenReturn(Completable.complete())
				.thenReturn(Completable.error(Exception("Connected twice")))
		}
		val testObserverResult = wifiManager.connect(correctSSID, correctPassword).test()
		testObserverResult.assertNoErrors()
		testObserverResult.assertComplete()

		val testObserverError = wifiManager.connect(correctSSID, correctPassword).test()
		testObserverError.assertErrorMessage("Connected twice")
	}

	@Test
	fun connect_givenTwoCorrect_completeFirstOnly() {
		//given
		val guessObservable = Observable.just(
			ConnectionAttempt(correctSSID, correctPassword),
			ConnectionAttempt(correctSSID, correctPassword))
		val guesser = mock<ConnectionGuesser> {
			on { guess(credImg) }.thenReturn(guessObservable)
		}
		val connectionManager = ConnectionManager(wifiManager, guesser)

		//when
		val testObserver = connectionManager.connect(credImg).test()

		//then
		testObserver.assertNoErrors()
		testObserver.assertComplete()
	}

	@Test
	fun connect_givenOneIncorrectAndTwoCorrect_completeSecondOnly() {
		//given
		val guessObservable = Observable.just(
			ConnectionAttempt(correctSSID, incorrectPassword),
			ConnectionAttempt(correctSSID, correctPassword),
			ConnectionAttempt(correctSSID, correctPassword)
		)
		val guesser = mock<ConnectionGuesser> {
			on { guess(credImg) }.thenReturn(guessObservable)
		}
		val connectionManager = ConnectionManager(wifiManager, guesser)

		//when
		val testObserver = connectionManager.connect(credImg).test()

		//then
		testObserver.assertNoErrors()
		testObserver.assertComplete()
	}
}
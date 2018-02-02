package com.elroid.wirelens.utils

import com.elroid.wirelens.framework.RoboelectricTest
import com.elroid.wirelens.util.FuzzyTextUtils
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import timber.log.Timber
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 *
 * Class: com.elroid.wirelens.TextParserTest
 * Project: WireLens
 * Created Date: 23/01/2018 15:39
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class FuzzyTextUtilsTest : RoboelectricTest() {

	@Test
	fun sortListBySimilarity_givenStringList_returnSorted() {
		//given/when
		val toMatch = "Closest"
		val list = listOf(
			"Cleanest", "Carrion", "Different", "Closet", "Diff", "Clod", "Clearest", "Comet", "Closer", "Closest", "Cleaner"
		)

		//then
		val expected = listOf(
			"Closest", "Closet", "Closer", "Clod", "Cleanest", "Clearest", "Comet", "Cleaner", "Carrion", "Different", "Diff"
		)
		val actual = FuzzyTextUtils.sortListBySimilarity(toMatch, list)
		//Timber.i("actual: %s", actual)
		assertArrayEquals(
			expected.toTypedArray(),
			actual.toTypedArray()
		)
	}

	@Test
	fun sortListBySimilarity_givenStringListMinScore_returnSorted() {
		//given/when
		val toMatch = "Closest"
		val list = listOf(
			"Cleanest", "Carrion", "Different", "Closet", "Diff", "Clod", "Clearest", "Comet", "Closer", "Closest", "Cleaner"
		)

		//then
		val expected = listOf(
			"Closest", "Closet", "Closer"
		)
		val actual = FuzzyTextUtils.sortListBySimilarity(toMatch, list, null, 70, 5)
		//Timber.i("actual: %s", actual)
		assertArrayEquals(
			expected.toTypedArray(),
			actual.toTypedArray()
		)
	}

	@Test
	fun sortListBySimilarity_givenStringMixedCase_returnSorted() {
		//given/when
		val toMatch = "Closest"
		val list = listOf(
			"CLOSET", "Diff", "Clod", "Clearest", "Comet", "Closer", "Closest", "Cleaner"
		)

		//then
		val expected = listOf(
			"Closest", "CLOSET", "Closer"
		)
		val actual = FuzzyTextUtils.sortListBySimilarity(toMatch, list, null, 70, 5)
		//Timber.i("actual: %s", actual)
		assertArrayEquals(
			expected.toTypedArray(),
			actual.toTypedArray()
		)
	}

	@Test
	fun sortListBySimilarity_givenStringListMinMax_returnSorted() {
		//given/when
		val toMatch = "Closest"
		val list = listOf(
			"Cleanest", "Carrion", "Different", "Closet", "Diff", "Clod", "Clearest", "Comet", "Closer", "Closest", "Cleaner"
		)

		//then
		val expected = listOf(
			"Closest", "Closet"
		)
		val actual = FuzzyTextUtils.sortListBySimilarity(toMatch, list, null, 70, 2)
		//Timber.i("actual: %s", actual)
		assertArrayEquals(
			expected.toTypedArray(),
			actual.toTypedArray()
		)
	}

	@Test
	fun sortListBySimilarity_givenObjectListMinScore_returnSorted() {
		//given/when
		val toMatch = "Closest"

		data class Narf(val name: String, val age: Int)

		val list = listOf(
			Narf("Closer", 23),
			Narf("Different", 2),
			Narf("Cleanest", 111),
			Narf("Closest", 0)
		)

		//then
		val expected = listOf(
			Narf("Closest", 0),
			Narf("Closer", 23),
			Narf("Cleanest", 111)
		)
		val actual = FuzzyTextUtils.sortListBySimilarity(toMatch, list, { it.name }, 60, 0)
		//Timber.i("actual: %s", actual)
		assertArrayEquals(
			expected.toTypedArray(),
			actual.toTypedArray()
		)
	}

	@Test
	fun matches_givenExamples_returnsCorrectly() {
		assertTrue(FuzzyTextUtils.matches("Something", "Something Else", 70))
		assertFalse(FuzzyTextUtils.matches("Something", "Else", 35))
		assertTrue(FuzzyTextUtils.matches("Something", "Somethong", 80))
		assertTrue(FuzzyTextUtils.matches("Something", "SOMETHING", 80))
		assertTrue(FuzzyTextUtils.matches("SSID", "S5ID", 70))
		assertTrue(FuzzyTextUtils.matches("It's a Trap", "It's a Trap ", 95))
		assertTrue(FuzzyTextUtils.matches("It's a Trap", "; It's a Trap ", 85))
	}

	@Test
	fun findSimilarLookingWords_givenExample1_returnsCorrectly() {
		val word = "SI"
		val expected = listOf("SI", "5I", "S1", "51", "Sl", "5l")
		val actual = FuzzyTextUtils.findSimilarLookingWords(word)
		assertArraysContainSameElements(expected, actual)
	}

	@Test
	fun findSimilarLookingWords_givenExample2_returnsCorrectly() {
		val word = "456"
		val expected = listOf("456", "A56", "4S6", "AS6", "45G", "A5G", "4SG", "ASG")
		val actual = FuzzyTextUtils.findSimilarLookingWords(word)
		assertArraysContainSameElements(expected, actual)
	}

	@Test
	fun findSimilarLookingWords_givenExample3_returnsCorrectly() {
		val word = "SSI"
		val expected = listOf(
			"SSI", "5SI", "S5I", "55I",
			"SSl", "5Sl", "S5l", "55l",
			"SS1", "5S1", "S51", "551"
		)
		val actual = FuzzyTextUtils.findSimilarLookingWords(word)
		assertArraysContainSameElements(expected, actual)
	}

	@Test
	fun findSimilarLookingWords_givenExample4_returnsCorrectly() {
		val word = "Password"
		val expected = listOf("Password")
		val actual = FuzzyTextUtils.findSimilarLookingWords(word)
		assertArraysContainSameElements(expected, actual)
	}

	private fun assertArraysContainSameElements(expected: List<String>, actual: List<String>) {
		Timber.d("assertArraysContainSameElements:\nExpected:%s\nActual  :%s", expected, actual)
		val expA = expected.sortedBy { it }
		val actA = actual.sortedBy { it }
		assertArrayEquals(expA.toTypedArray(), actA.toTypedArray())
	}

}
package com.elroid.wirelens.utils

import com.elroid.wirelens.framework.RoboelectricTest
import com.elroid.wirelens.util.FuzzyTextUtils
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFalse

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
		assertFalse(FuzzyTextUtils.matches("Something", "Else", 20))
		assertTrue(FuzzyTextUtils.matches("Something", "Somethong", 80))
		assertTrue(FuzzyTextUtils.matches("SSID", "S5ID", 70))
		assertTrue(FuzzyTextUtils.matches("It's a Trap", "It's a Trap ", 95))
		assertTrue(FuzzyTextUtils.matches("It's a Trap", "; It's a Trap ", 85))
	}
}
package com.elroid.wirelens.util

import me.xdrop.fuzzywuzzy.FuzzySearch
import timber.log.Timber
import java.util.Locale
import kotlin.math.min

/**
 * Class: com.elroid.wirelens.util.FuzzyTextUtils
 * Project: WireLens
 * Created Date: 24/01/2018 09:28
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
object FuzzyTextUtils {

	private val allAlts = mutableListOf<Char>()
	private val altMap = mutableMapOf<Char, CharAlt>()

	private class CharAlt(vararg val alts: Char)

	init {
		val charAlts = mutableListOf(
			CharAlt('O', 'D', '0'),
			CharAlt('1', 'I', 'l'),
			CharAlt('4', 'A'),
			CharAlt('5', 'S'),
			CharAlt('6', 'G'),
			CharAlt('8', 'B'),
			CharAlt('M', 'H')
		)
		charAlts.forEach { charAlt ->
			charAlt.alts.forEach {
				allAlts.add(it)
				altMap[it] = charAlt
			}
		}
	}

	/*fun getClosestMatch(toMatch: String?,
						candidates: Array<String?>): Pair<String, Int> {
		val result =
			FuzzySearch.extractOne(toMatch, Arrays.asList<String>(*candidates))
		return Pair(result.string, result.score)
	}*/

	fun <T> sortListBySimilarity(toMatch: String,
								 list: List<T>,
								 minScore: Int = 0,
								 maxLen: Int = list.size,
								 extractor: (T) -> String = { obj ->
									 obj?.toString() ?: ""
								 }): List<T> {
		val names = mutableListOf<String>()
		val listHash = mutableMapOf<String, T>()
		for(i in list.indices) {
			val t = list[i]
			val name = extractor(t).toUpper()
			names.add(name)
			listHash[name] = t
		}
		val sorted = FuzzySearch.extractSorted(toMatch.toUpper(), names)
		val result = mutableListOf<T>()
		val maxLength = min(sorted.size, maxLen)
		for(i in 0 until maxLength) {
			val extractedResult = sorted[i]
			Timber.d("result[%s]: string:%s score:%s", i,
				extractedResult.string, extractedResult.score)
			if(extractedResult.score >= minScore) {
				listHash.remove(extractedResult.string)?.let {
					result.add(it)
				}
			} else break
		}
		return result
	}

	private fun String.toUpper(): String = toUpperCase(Locale.getDefault())

	@JvmStatic
	fun matches(str1: String, str2: String, minSimilarity: Int): Boolean {
		val sim = FuzzySearch.ratio(str1.toUpper(), str2.toUpper())
		Timber.v("matches(%s, %s): %s", str1, str2, sim)
		return sim >= minSimilarity
	}

	fun findSimilarLookingWords(word: String): List<String> {
		val result = mutableListOf<String>()
		result.add(word)
		for(i in word.indices) {
			val c = word[i]
			if(allAlts.contains(c)) {
				val similarWordsForIndex = mutableListOf<String>()
				for(simWord in result) {
					val similarWords = findSimilarLookingWords(simWord, i)
					//Timber.d("found similar(%s) to %s: %s", i, simWord, similars);
					similarWordsForIndex.addAll(similarWords)
				}
				result.addAll(similarWordsForIndex)
			}
		}
		return result
	}

	private fun findSimilarLookingWords(word: String,
										index: Int): List<String> {
		val c = word[index]
		val alt = altMap[c]
		val result = mutableListOf<String>()
		alt?.alts?.forEach {
			val sim = setCharAt(word, index, it)
			if(word != sim && !result.contains(sim))
				result.add(sim)
		}
		return result
	}

	private fun setCharAt(word: String, index: Int, c: Char): String {
		return word.substring(0, index) + c + word.substring(index + 1)
	}

}
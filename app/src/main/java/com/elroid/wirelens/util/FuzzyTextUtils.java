package com.elroid.wirelens.util;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.util.FuzzyTextUtils
 * Project: WireLens
 * Created Date: 24/01/2018 09:28
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
//this is a util class, methods should be public and may not yet be used
public class FuzzyTextUtils
{
	public static Pair<String, Integer> getClosestMatch(String toMatch, String[] candidates){
		ExtractedResult result = FuzzySearch.extractOne(toMatch, Arrays.asList(candidates));
		return new Pair<>(result.getString(), result.getScore());
	}

	public interface NameExtractor<T>
	{
		String getName(T obj);
	}

	public static <T> List<T> sortListBySimilarity(String toMatch, List<T> list){
		return sortListBySimilarity(toMatch, list, null);
	}

	public static <T> List<T> sortListBySimilarity(String toMatch,
												   List<T> list,
												   NameExtractor<T> extractor){
		return sortListBySimilarity(toMatch, list, extractor, 0, list.size());
	}

	public static <T> List<T> sortListBySimilarity(String toMatch,
												   List<T> list,
												   NameExtractor<T> extractor,
												   int minScore,
												   int maxLen){
		if(extractor == null)
			extractor = obj -> (obj == null) ? "" : obj.toString();

		List<String> names = new ArrayList<>(list.size());
		ArrayMap<String, T> listHash = new ArrayMap<>(list.size());
		for(int i = 0; i < list.size(); i++){
			T t = list.get(i);
			String name = extractor.getName(t).toUpperCase();
			names.add(name);
			listHash.put(name, t);
		}
		List<ExtractedResult> sorted = FuzzySearch.extractSorted(toMatch.toUpperCase(), names);
		List<T> result = new ArrayList<>(list.size());
		if(maxLen == 0) maxLen = list.size();
		int MAX_LEN = Math.min(sorted.size(), maxLen);
		for(int i = 0; i < MAX_LEN; i++){
			ExtractedResult extractedResult = sorted.get(i);
			if(extractedResult.getScore() >= minScore){
				/*Timber.d("result[%s]: string:%s score:%s", i,
					extractedResult.getString(), extractedResult.getScore());*/
				result.add(listHash.remove(extractedResult.getString()));
			}
			else
				break;
		}
		return result;
	}

	public static boolean matches(String str1, String str2, int minSimilarity){
		int sim = FuzzySearch.ratio(str1.toUpperCase(), str2.toUpperCase());
		Timber.v("matches(%s, %s): %s", str1, str2, sim);
		return sim >= minSimilarity;
	}

}

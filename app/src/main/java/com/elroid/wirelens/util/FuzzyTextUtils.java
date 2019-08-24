package com.elroid.wirelens.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.collection.ArrayMap;
import androidx.core.util.Pair;
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

	private static class CharAlt
	{
		private char[] alts;

		public CharAlt(char... alts){
			this.alts = alts;
		}
	}

	private static List<Character> allAlts;
	private static ArrayMap<Character, CharAlt> altMap;

	static{
		List<CharAlt> charAlts = new ArrayList<>(20);
		charAlts.add(new CharAlt('O', 'D', '0'));
		charAlts.add(new CharAlt('1', 'I', 'l'));
		charAlts.add(new CharAlt('4', 'A'));
		charAlts.add(new CharAlt('5', 'S'));
		charAlts.add(new CharAlt('6', 'G'));
		charAlts.add(new CharAlt('8', 'B'));
		charAlts.add(new CharAlt('M', 'H'));
		allAlts = new ArrayList<>();
		altMap = new ArrayMap<>();
		for(int i = 0; i < charAlts.size(); i++){
			CharAlt charAlt = charAlts.get(i);
			for(char c : charAlt.alts){
				allAlts.add(c);
				altMap.put(c, charAlt);
			}
		}
	}

	public static List<String> findSimilarLookingWords(String word){
		List<String> result = new ArrayList<>();
		result.add(word);
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(allAlts.contains(c)){
				List<String> similarsForIndex = new ArrayList<>();
				for(String simWord : result){
					List<String> similars = findSimilarLookingWords(simWord, i);
					//Timber.d("found similar(%s) to %s: %s", i, simWord, similars);
					similarsForIndex.addAll(similars);
				}
				result.addAll(similarsForIndex);
			}
		}
		return result;
	}

	/*private static List<String> removeDuplicates(List<String> list){
		List<String> result = new ArrayList<>(list.size());
		Timber.d("pre-dup :%s", list);
		for(String s : list){
			if(!result.contains(s))
				result.add(s);
		}
		Timber.d("post-dup:%s", result);
		return result;
	}*/

	private static List<String> findSimilarLookingWords(String word, int index){
		char c = word.charAt(index);
		CharAlt alt = altMap.get(c);
		List<String> result = new ArrayList<>(alt.alts.length);
		for(int i = 0; i < alt.alts.length; i++){
			char a = alt.alts[i];
			String sim = setCharAt(word, index, a);
			if(!word.equals(sim))
				result.add(sim);
		}
		return result;
	}

	private static String setCharAt(String word, int index, char c){
		return word.substring(0, index) + c + word.substring(index + 1);
	}

}

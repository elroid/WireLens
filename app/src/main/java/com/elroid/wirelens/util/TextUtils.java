package com.elroid.wirelens.util;

import java.util.regex.Pattern;

/**
 * Class: com.elroid.wirelens.util.TextUtils
 * Project: WireLens
 * Created Date: 24/01/2018 09:28
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
//this is a util class, methods should be public and may not yet be used
public class TextUtils
{
	public static String getValueFromLineStartingWith(String label, String[] lines){
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			int index;
			if((index = indexOfIgnoreCase(line, label)) != -1){
				String tail = line.substring(index + label.length()).trim();
				while(tail.startsWith(":"))
					tail = tail.substring(1);
				if(tail.trim().equals("")){
					//assume it is the next line
					if(i + 1 < lines.length){
						return lines[i + 1].trim();
					}
					else
						return "";
				}
				return tail.trim();
			}
		}
		return null;
	}

	public static int indexOfIgnoreCase(String str, String tok){
		String strUpper = str.toUpperCase();
		String tokUpper = tok.toUpperCase();
		return strUpper.indexOf(tokUpper);
	}

	public static boolean containsIgnoreCase(String str, String tok){
		return indexOfIgnoreCase(str, tok) > 0;
	}

	public static boolean equalsIgnoreCase(String str, String tok){
		return str.toUpperCase().equals(tok.toUpperCase());
	}

	private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

	public static boolean isHex(String str){
		return str != null && HEX_DIGITS.matcher(str).matches();
	}
}

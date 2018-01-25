package com.elroid.wirelens.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.util.Printer
 * Project: WireLens
 * Created Date: 24/01/2018 14:02
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class Printer
{
	public static String print(Bundle b) {
		String result = "Bundle: ";
		if(b != null && !b.isEmpty()){
			Set<String> keys = b.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				result += "\nkey("+key+") val("+print(b.get(key))+")";
			}
		}
		else result += "(Empty)";
		return result;
	}

	public static String print(List list) {
		String result = "List(";
		if (list == null) return result + "null)";
		result += list.size() + "):";
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			result += "\n[" + i + "]=" + print(o);
		}
		return result;
	}

	public static String printInline(List list) {
		String result = "List(";
		if (list == null) return result + "null)";
		result += list.size() + "):";
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if(i != 0) result += ",";
			result += print(o);
		}
		return result;
	}

	public static String print(Map hash) {
		return print(hash, 0);
	}
	public static String print(Map hash, int indent) {
		if (hash == null) return "Map: null";
		String result = "Map["+hash.getClass()+"](";
		result += hash.size() + "):";
		Set keys = hash.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			Object key = it.next();
			Object val = hash.get(key);
			result += "\n" + pad(indent) + "[" + key + "]=";
			result += print(val, indent + 1);
		}
		return result;
	}
	private static String print(Object obj, int indent){
		if (obj == null) return null;
		else {
			if (obj instanceof Map) {
				return print((Map)obj, indent);
			}
			/*else if (obj instanceof Object[]) {
				return print((Object[]) obj, indent);
			}*/
			return print(obj);
		}
	}

	public static String print(Intent intent) {
		if(intent == null)
			return "Intent: null";

		String result = "Intent: "+intent.toString();
		try {
			Bundle b = intent.getExtras();
			if(b != null && !b.isEmpty()){
				result += "\nExtras: ";
				Set<String> keys = b.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object o = b.get(key);
					result += "\nkey("+key+") val("+print(o)+")";
				}
			}
		} catch (Exception e) {
			result += "\nError: "+e.getMessage();
			Timber.w(e, e.getMessage());
		}
		return result;
	}

	public static String print(View v) {
		if(v == null) return null;
		if(v instanceof ViewGroup)
			return print((ViewGroup)v);
		else {
			String result;
			if(v instanceof TextView)
				result = "TextView: " + ((TextView) v).getText();
			else
				result = v.toString() + "[id: " + v.getId() + "]";

			result += ", dims: " + v.getWidth() + "x" + v.getHeight();
			result += ", visibility: " + v.getVisibility();
			result += ", padding: l("+v.getPaddingLeft()+"/"+v.getPaddingStart()
				+") r("+v.getPaddingRight()+"/"+v.getPaddingEnd()
				+") t("+v.getPaddingTop()+") b("+v.getPaddingBottom()+")";
			return result;
		}
	}
	public static String print(ViewGroup vg) {
		return print(vg, 0);
	}

	public static String print(ViewGroup vg, int indent) {
		if (vg == null) return "null";
		String result = "\n"+vg.getClass()+" (";
		result += vg.getChildCount()+")";
		result += ", dims: " + vg.getWidth() + "x" + vg.getHeight();
		result += ", visibility: "+vg.getVisibility();
		result += ", padding: l("+vg.getPaddingLeft()+"/"+vg.getPaddingStart()
			+") r("+vg.getPaddingRight()+"/"+vg.getPaddingEnd()
			+") t("+vg.getPaddingTop()+") b("+vg.getPaddingBottom()+")";
		//result += " [ "+vg.toString()+"] ";
		result += " [ "+vg.getId()+"] ";
		result += "):";
		for (int i = 0; i < vg.getChildCount(); i++) {
			View o = vg.getChildAt(i);
			if (o != null) {
				result += "\n" + pad(indent) + "[" + i + "]=";
				if (o instanceof ViewGroup) result += print((ViewGroup) o, indent + 1);
				else result += print(o);
			}
		}
		return result;
	}
	private static String pad(int indent) {
		StringBuilder result = new StringBuilder();
		if(indent > 0)
			result.append(".");//if we don't do this the pad gets swallowed into the newline
		for (int i = 0; i < indent; i++)
			result.append("    ");
		//result.append("\t");
		return result.toString();
	}

	public static String print(Object obj) {
		if (obj == null) return null;
		else {
			if (obj instanceof List) {
				return print((List) obj);
			}
			else if (obj instanceof Bundle) {
				return print((Bundle) obj);
			}
			else if (obj instanceof Map) {
				return print((Map) obj);
			}
			else if (obj instanceof Intent) {
				return print((Intent) obj);
			}
			else if (obj instanceof ViewGroup) {
				return print((ViewGroup) obj);
			}
			else if (obj instanceof View) {
				return print((View) obj);
			}
			else if (obj instanceof float[]) {
				return print((float[]) obj);
			}
			else if (obj instanceof int[]) {
				return print((int[]) obj);
			}
			else if (obj instanceof Object[]) {
				return print((Object[]) obj);
			}
			else return obj.toString() + " (" + obj.getClass() + ")";
		}
	}

	public static void printStack(String msg){
		try{throw new Exception("Stack trace: "+msg+" ("+Thread.currentThread()+")");}
		catch (Exception e){
			Timber.d(msg);
			//e.printStackTrace();
			StackTraceElement[] elems = e.getStackTrace();
			for (int i = 0; i < elems.length; i++) {
				StackTraceElement elem = elems[i];
				Timber.v("\tat %s", elem);
			}
		}
	}

	public static String printCallingMethod(){
		return printMethod(3, 0);
	}
	/*public static String printCallingMethod(int levelCorrect){
		return printMethod(3 - levelCorrect, 0);
	}
	public static String printCallingMethod(int levelCorrect, int levels){
		return printMethod(3-levelCorrect, levels);
	}*/
	public static String printMethodStack(int levels){
		return printMethod(2,levels);
	}
	private static String printMethod(int start, int levels){
		try{throw new Exception("Stack trace: ("+Thread.currentThread()+")");}
		catch (Exception e){
			StackTraceElement[] elems = e.getStackTrace();
			/*for (int i = 0; i < elems.length; i++) {
				StackTraceElement elem = elems[i];
				logger.debug("["+i+"] "+elem);
			}*/
			if(levels == 0)
				return elems[start].toString();
			else{
				StringBuilder r = new StringBuilder("Methods:");
				for (int i = start;
					 i < elems.length && i < start+levels;
					 i++) {
					StackTraceElement elem = elems[i];
					r.append("\n[").append(i).append("]: ").append(elem);
				}
				return r.toString();
			}
		}
	}

	public static String print(Object[] list) {
		if (list == null) return "null";
		else if(list.length == 0) return "Empty";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			Object o = list[i];
			if(i != 0) result.append(",");
			if(o == null) result.append("null");
			else result.append(o.toString());
		}
		return result.toString();
	}

	public static String print(Object[] list, int indent) {
		if (list == null) return "null";
		else if(list.length == 0) return "Empty";
		String result = "Object["+list.length+"]:";
		for (int i = 0; i < list.length; i++) {
			Object o = list[i];
			if (o != null) {
				result += "\n" + pad(indent) + "[" + i + "]=";
				result += print(o, indent + 1);
			}
		}
		return result;
	}
	/*public static String print(Object[] list, int indent) {
		String result = "\nArray(";
		if (list == null) return result + "null)";
		result += list.length + "):";
		for (int i = 0; i < list.length; i++) {
			Object o = list[i];
			if (o != null) {
				result += "\n" + pad(indent) + "[" + i + "]=";
				result += print(o, indent + 1);
			}
		}
		return result;
	}*/

	public static String printThread(){
		StringBuilder result = new StringBuilder();
		result.append(Thread.currentThread());
		if(GenUtils.isUIThread())
			result.append(" (Main thread)");
		return result.toString();
	}

	public static String print(float[] list) {
		return print(list, 0, list.length);
	}

	public static String print(float[] list, int start, int len) {
		String result = "float[";
		if (list == null) return result + "null]";
		result += list.length + "]";
		if (start + len > list.length)
			start = list.length - len;
		int lim = Math.min(start + len, list.length);
		if(start != 0 || len != list.length)
			result += " (" + start + "-" + (start + len) + ")";
		result += ": ";
		for (int i = start; i < lim; i++) {
			float o = list[i];
			if (i != 0) result += ", ";
			result += "" + o;
		}
		return result;
	}

	public static String print(int[] list) {
		return print(list, 0, list.length);
	}

	public static String print(int[] list, int start, int len) {
		String result = "int[";
		if (list == null) return result + "null]";
		result += list.length + "]";
		if (start + len > list.length)
			start = list.length - len;
		int lim = Math.min(start + len, list.length);
		if(start != 0 || len != list.length)
			result += " (" + start + "-" + (start + len) + ")";
		result += ": ";
		for (int i = start; i < lim; i++) {
			int o = list[i];
			if (i != 0) result += ", ";
			result += "" + o;
		}
		return result;
	}
}

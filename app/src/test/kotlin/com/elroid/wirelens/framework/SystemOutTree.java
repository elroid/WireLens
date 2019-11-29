package com.elroid.wirelens.framework;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.SoutTree
 * Project: WireLens
 * Created Date: 31/01/2018 15:51
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class SystemOutTree extends Timber.Tree
{
	private int logLevel;

	public SystemOutTree(int logLevel){
		this.logLevel = logLevel;
	}

	@Override
	protected boolean isLoggable(@Nullable String tag, int priority){
		return priority >= logLevel;
	}

	@Override
	protected void log(int priority, String tag, @NonNull String message, Throwable t){
		//15:47:28.123 - ClassName: DEBUG/My Message here
		//String msg = new Date().toString();
		String msg = new GregorianCalendar().toZonedDateTime()
			.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
		if(tag != null)
			msg += " - " + tag;
		msg += ": ";
		msg += print(priority) + "/";
		msg += message;

		System.out.println(msg);
	}


	private String print(int priority){
		switch(priority){
			case Log.VERBOSE:
				return "TRACE";
			case Log.DEBUG:
				return "DEBUG";
			case Log.WARN:
				return "WARN ";
			case Log.ERROR:
				return "ERROR";
			default:
				return "? (" + priority + ")";
		}
	}

}

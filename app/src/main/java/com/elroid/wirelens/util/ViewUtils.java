package com.elroid.wirelens.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.view.TouchDelegate;
import android.view.View;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.util.ViewUtils
 * Project: WireLens
 * Created Date: 22/01/2018 16:34
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
//this is a util class, methods should be public and may only be used once or not at all
public class ViewUtils
{
	public static float pxToDp(float px){
		float density = Resources.getSystem().getDisplayMetrics().density;
		return px / density;
	}

	public static float dpToPx(float dp){
		float density = Resources.getSystem().getDisplayMetrics().density;
		return dp * density;
	}

	public static int pxToDpInt(float px){
		return Math.round(pxToDp(px));
	}

	public static int dpToPxInt(float dp){
		return Math.round(dpToPx(dp));
	}

	public static @ColorInt int color(@ColorRes int colorResID, View v){
		return color(colorResID, v.getContext());
	}

	@SuppressWarnings("deprecation")
	public static @ColorInt int color(@ColorRes int colorResID, Context ctx){
		Resources r = ctx.getResources();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			Resources.Theme t = ctx.getTheme();
			return r.getColor(colorResID, t);
		}
		else{
			return r.getColor(colorResID);
		}
	}

	private static final int FADE_DURATION = 250;

	public static void fade(boolean fadeIn, final View view){
		//Printer.printStack("fade("+fadeIn+","+view+")");
		if(view == null) return;
		view.post(() -> {
			if(fadeIn){
				if(view.getVisibility() == View.VISIBLE){
					Timber.v("No need to fade in already visible object: %s", view);
					return;
				}
				view.animate().alpha(1)
						.setDuration(FADE_DURATION)
						.withStartAction(() -> {
							view.setAlpha(0);
							view.setVisibility(View.VISIBLE);
						})
						.start();
			}
			else{
				if(view.getVisibility() != View.VISIBLE){
					Timber.v("No need to fade out already invisible(%s) object: %s", view.getVisibility(), view);
					return;
				}
				view.animate().alpha(0)
						.setDuration(FADE_DURATION)
						.withEndAction(() -> {
							view.setVisibility(View.INVISIBLE);
							view.setAlpha(1);
						})
						.start();
			}
		});
	}

	public static Handler handler(){
		return new Handler(Looper.getMainLooper());
	}

	public static void increaseHitArea(View view, @Dimension(unit = Dimension.DP) int dpToIncrease){
		if(view == null) return;
		final View parent = (View) view.getParent();  // button: the view you want to enlarge hit area
		final int pxToIncrease = dpToPxInt(dpToIncrease);
		parent.post(() -> {
			final Rect rect = new Rect();
			view.getHitRect(rect);
			rect.top -= pxToIncrease;    // increase top hit area
			rect.left -= pxToIncrease;   // increase left hit area
			rect.bottom += pxToIncrease; // increase bottom hit area
			rect.right += pxToIncrease;  // increase right hit area
			parent.setTouchDelegate(new TouchDelegate(rect, view));
		});
	}

	public static Bitmap createBitmap(Drawable drawable, int width, int height){
		if(drawable instanceof BitmapDrawable){
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static Bitmap createBitmap(Drawable drawable){
		if(drawable instanceof BitmapDrawable){
			return ((BitmapDrawable) drawable).getBitmap();
		}
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static int getScreenWidth(Context ctx){
		return getScreenDim(true);
	}

	public static int getScreenHeight(Context ctx){
		return getScreenDim(false);
	}

	private static int getScreenDim(boolean width){
		try{
			int w = Resources.getSystem().getDisplayMetrics().widthPixels;
			int h = Resources.getSystem().getDisplayMetrics().heightPixels;
			return width ? w : h;
		}
		catch(Exception e){
			Timber.w(e, "Problem getting screen width - returning 0");
			return 0;
		}
	}

	public static boolean isLandscape(Context ctx){
		return getScreenWidth(ctx) > getScreenHeight(ctx);
	}
}

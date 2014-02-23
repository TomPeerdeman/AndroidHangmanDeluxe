/**
 * File: HangmanDrawable.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanDrawable extends Drawable {
	private final Bitmap mainBitmap;
	private final Paint paint;
	
	private Bitmap secBitmap;
	private int secOffsX;
	private int secOffsY;
	
	/**
	 * @param mainBitmap
	 */
	public HangmanDrawable(Bitmap mainBitmap) {
		this.mainBitmap = mainBitmap;
		
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	
	/**
	 * @param secBitmap
	 * @param secOffsetX
	 * @param secOffsetY
	 */
	public void setSecBitmap(Bitmap secBitmap, int secOffsetX, int secOffsetY) {
		this.secBitmap = secBitmap;
		this.secOffsX = secOffsetX;
		this.secOffsY = secOffsetY;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mainBitmap, new Rect(0, 0, mainBitmap.getWidth(),
				mainBitmap.getHeight()), getBounds(), paint);
		
		double scaleX =
			(double) getBounds().width() / (double) mainBitmap.getWidth();
		double scaleY =
			(double) getBounds().height() / (double) mainBitmap.getHeight();
		
		int secWidth = (int) Math.round(secBitmap.getWidth() * scaleX);
		int secHeight =
			(int) Math.round((secBitmap.getHeight() * scaleY * getLevel()) / 100.0);
		
		int secStartX =
			(int) Math.round((138 + secOffsX) * scaleX) - (secWidth / 2);
		int secStartY = (int) Math.round((50 + secOffsY) * scaleY);
		
		int secSrcHeight =
			(int) Math.round(secBitmap.getHeight() * getLevel() / 100.0);
		
		Rect secOrig = new Rect(0, 0, secBitmap.getWidth(), secSrcHeight);
		Rect secNew =
			new Rect(secStartX, secStartY, secStartX + secWidth, secStartY
					+ secHeight);
		
		canvas.drawBitmap(secBitmap, secOrig, secNew, paint);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.graphics.drawable.Drawable#setAlpha(int)
	 */
	@Override
	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.
	 * ColorFilter)
	 */
	@Override
	public void setColorFilter(ColorFilter cf) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.graphics.drawable.Drawable#getOpacity()
	 */
	@Override
	public int getOpacity() {
		return PixelFormat.UNKNOWN;
	}
}

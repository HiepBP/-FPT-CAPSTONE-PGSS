package com.fptuni.capstone.pgss.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.fptuni.capstone.pgss.R;

/**
 * Created by TrungTNM on 2/9/2017.
 */

public class MapMarkerHelper {

    private MapMarkerHelper() {
    }

    public static Bitmap getParkingMarker(Context context, int availableLot) {
        String text = String.valueOf(availableLot);

        int drawableId;

        if (isBetween(availableLot, 0, 10)) {
            drawableId = R.drawable.map_parking_marker_red;
        } else if (isBetween(availableLot, 11, 30)) {
            drawableId = R.drawable.map_parking_marker_yellow;
        } else {
            drawableId = R.drawable.map_parking_marker_green;
        }

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(context, 14));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bmp);

        // In case the text is bigger than the canvas, reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4)) {
            paint.setTextSize(convertToPixels(context, 10));
        }

        // Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;
        // ((paint.descent() + paint.ascent()) / 2) is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bmp;
    }

    private static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}

package com.xue.siu.common.view.imagespan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * Created by XUE on 2016/2/1.
 */
public class OSImageSpan extends ImageSpan {

    public OSImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public OSImageSpan(Drawable d) {
        super(d);
    }

    public OSImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public OSImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public OSImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }


    public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}

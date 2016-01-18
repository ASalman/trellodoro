package com.asalman.trellodoro.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by asalman on 1/18/16.
 */
public class DigitalTextView extends TextView {

    private static Typeface sDigitalTypeFace;

    public DigitalTextView(Context context) {
        this(context, null);
    }

    public DigitalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
        setTypeface();
    }

    private void setTypeface() {
        if (sDigitalTypeFace == null) {
            sDigitalTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/digital.ttf");
        }
        setTypeface(sDigitalTypeFace);
    }
}

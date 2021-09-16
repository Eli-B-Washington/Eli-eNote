package com.example.enote;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * NoUnderlineSpan Class
 * This class allows underline to be removed from text
 */
public class NoUnderlineSpan extends UnderlineSpan {
    public NoUnderlineSpan() {}
    public NoUnderlineSpan(Parcel src) {}
    @Override

    /**
     * The updateDrawState Method
     * Purpose: to remove the underline from the text
     * @param ds - TextPaint
     */
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
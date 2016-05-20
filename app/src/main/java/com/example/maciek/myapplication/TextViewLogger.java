package com.example.maciek.myapplication;

import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by maciek on 2016-05-20.
 */
public class TextViewLogger {
    TextView textView;

    public TextViewLogger(TextView textView) {
        if(textView == null) {
            throw new RuntimeException("NULL TEXTVIEW");
        }
        this.textView = textView;

    }

    public void log(String pattern, Object... args) {
        appendText(pattern, args);
    }
    public void important(String pattern, Object... args) {
        appendColoredText(Color.GREEN, pattern, args);
    }

    public void warn(String pattern, Object... args) {
        appendColoredText(Color.YELLOW, pattern, args);
    }
    public void info(String pattern, Object... args) {
        appendColoredText(Color.BLUE, pattern, args);
    }
    public void error(String pattern, Object... args) {
        appendColoredText(Color.RED, pattern, args);
    }


    private void appendText(String pattern, Object ... args) {
        String result = String.format(pattern, args);
        textView.append(result + "\n");
    }

    public void appendColoredText(int color, String pattern, Object ... args) {
        int start = textView.getText().length();
        this.appendText(pattern, args);
        int end = textView.getText().length();
        Spannable spannableText = (Spannable) textView.getText();
        spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
    }
}

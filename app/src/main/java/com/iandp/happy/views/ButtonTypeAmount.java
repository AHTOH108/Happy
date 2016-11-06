package com.iandp.happy.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.utils.AmountParser;

/**
 * Created on 03.11.2016.
 */

public class ButtonTypeAmount extends RelativeLayout {

    private TextView mTextView;

    private byte currentType = 1;

    public ButtonTypeAmount(Context context) {
        super(context);
        if (!isInEditMode())
            initControl(context);
    }

    public ButtonTypeAmount(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            initControl(context);
    }

    public ButtonTypeAmount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode())
            initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater li = ((Activity) getContext()).getLayoutInflater();
        li.inflate(R.layout.button_type_amount, this);

        mTextView = (TextView) findViewById(R.id.textView);
        showText();
    }

    private void showText() {
        mTextView.setText(AmountParser.getShortNameType(currentType));
    }

    public void nextTypeAmount() {
        currentType = AmountParser.getNextType(currentType);
        showText();
    }

    public byte getTypeAmount() {
        return currentType;
    }
}

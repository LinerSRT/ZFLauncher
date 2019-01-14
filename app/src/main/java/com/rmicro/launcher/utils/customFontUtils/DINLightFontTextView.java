package com.rmicro.launcher.utils.customFontUtils;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Tim
 */
public class DINLightFontTextView extends AppCompatTextView {

    public DINLightFontTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontManager.DINLight);
    }

    public DINLightFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DINLightFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}

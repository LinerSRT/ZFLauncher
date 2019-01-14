package com.rmicro.launcher.utils.customFontUtils;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Tim.
 */
public class FZLFontEditText extends AppCompatEditText {
    public FZLFontEditText(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //AssetManager assetManager = context.getAssets();
        //Typeface font = Typeface.createFromAsset(assetManager, "FZLTXHK.TTF");
        setTypeface(FontManager.FZLTXHK);
    }

    public FZLFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FZLFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

package com.rmicro.launcher.utils.customFontUtils;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Tim.
 */
public class FZLFontTextView extends AppCompatTextView {

    public FZLFontTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //AssetManager assetManager = context.getAssets();
        //Typeface font = Typeface.createFromAsset(assetManager, "FZLTXHK.TTF");
        setTypeface(FontManager.FZLTXHK);
    }

    public FZLFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FZLFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}

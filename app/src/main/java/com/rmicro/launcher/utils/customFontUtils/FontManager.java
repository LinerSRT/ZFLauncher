package com.rmicro.launcher.utils.customFontUtils;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.rmicro.launcher.utils.LogUtil;

/**
 * Created by tim on 16-10-25.//字体管理
 */
public class FontManager {

    private static FontManager FONT_MANAGER;
    private AssetManager mAssetManager;

    private FontManager(Application application) {
        mAssetManager = application.getAssets();
    }
    public static void createInstance(Application application) {
        if (FONT_MANAGER == null) {
            synchronized (FontManager.class) {
                if (FONT_MANAGER == null) {
                    FONT_MANAGER = new FontManager(application);
                }
            }
        }
    }
    public static FontManager getInstance() {
        if (FONT_MANAGER == null) {
            throw new NullPointerException("NullPoint Exception, Please call createInstance first!");
        }
        return FONT_MANAGER;
    }


    public static Typeface DINLight;
    public static Typeface DINRegular;
    public static Typeface FZLTXHK;
    public static Typeface FZLTBlodBlack;

    public void initFontType(){
        LogUtil.d("FontManager",">>>> initFontType.....");
        DINLight = Typeface.createFromAsset(mAssetManager, "fonts/DINNextLTPro-Light.otf");
        DINRegular = Typeface.createFromAsset(mAssetManager, "fonts/DINNextLTPro-Regular.otf");
        FZLTXHK = Typeface.createFromAsset(mAssetManager, "fonts/FZLTXHK.TTF");
        FZLTBlodBlack = Typeface.createFromAsset(mAssetManager, "fonts/LanTingBoldBlack.TTF");
    }

    public static enum FontType{
        DINLight,
        DINRegular,
        FZLTXHK,
        FZLTBlodBlack,
    }

}

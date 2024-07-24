package com.app.pokeapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtils {

    //Be aware that this utility method ONLY works when called from an Activity
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }


    //for hiding the keyboard from fragments
    public static void hideKeyboardFrom(Context context,
                                        View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public static int getThemePrimaryColor(Context context) {
        int        colorAttr = android.R.attr.colorPrimary;
        TypedValue outValue  = new TypedValue();
        context.getTheme()
               .resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }
}

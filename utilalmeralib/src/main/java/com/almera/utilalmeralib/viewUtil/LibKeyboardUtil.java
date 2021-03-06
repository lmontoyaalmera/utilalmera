package com.almera.utilalmeralib.viewUtil;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class LibKeyboardUtil {

    public static void hideKeyboard(Context context) {
        if (((Activity)context).getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
        }
    }

}

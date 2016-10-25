package com.tektonlabs.taskview.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by JoshAndre on 25/10/2016.
 */
public class UIHelper {

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = ((Activity) context).getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }
}

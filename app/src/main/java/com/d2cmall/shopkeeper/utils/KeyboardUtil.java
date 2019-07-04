package com.d2cmall.shopkeeper.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者:Created by sinbara on 2019/2/22.
 * 邮箱:hrb940258169@163.com
 */

public class KeyboardUtil {

    public static void showKeyboard(final View view) {
        view.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyboard(final View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

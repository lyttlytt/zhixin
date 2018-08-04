package com.library.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.library.R;


/**
 * Author：袁从斌 on 2017/4/14 14:51
 */

public class SnackbarUtils {
    /**
     * show snackbar duration is short
     * @param view The view to find a parent from.
     * @param message show text
     * default snackbar background colors.xml
     * @see #getColorPrimaryDarkColor(Context)
     */
    public static void showSnackbarShort(View view,String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getColorPrimaryDarkColor(view.getContext()));
        snackbar.show();
    }

    /**
     * show snackbar duration is long
     * @param view The view to find a parent from
     * @param message show text
     * default sanckbar background colors.xml
     * @see #getColorPrimaryDarkColor(Context)
     */
    public static void showSnackbarLong(View view,String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getColorPrimaryDarkColor(view.getContext()));
        snackbar.show();
    }

    /**
     * show snackbar duration is indefinite
     * @param view The view to find a parent from
     * @param message show text
     * default sanckbar background colors.xml
     *  @see #getColorPrimaryDarkColor(Context)
     */
    public static void showSnackbarIndefinite(View view,String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setBackgroundColor(getColorPrimaryDarkColor(view.getContext()));
        snackbar.show();
    }
    public static int getColorPrimaryDarkColor(Context context){
        return ContextCompat.getColor(context, R.color.colorPrimaryDark);
    }

}

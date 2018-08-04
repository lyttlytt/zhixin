package com.shuzhengit.zhixin.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.library.util.ToastUtils;
import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/7 09:46
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:对话框的集合
 */

public class DialogUtil {
    public interface DialogCallBack {
        void callBackNegative(DialogInterface dialog);

        void callBackPositive(DialogInterface dialog);
    }

    public interface DialogEditTextCallBack {
        void callBackNegative(DialogInterface dialog);

        void callBackPositive(DialogInterface dialog, String inputMessage);
    }

    public interface DialogSingleCallBack {
        void callBackPositive(DialogInterface dialog);
    }

    public interface DialogInputCallBack {
        void callBackPositive(DialogInterface dialogInterface, String callbackMessage);
    }

    /**
     * v7包下的dialog 有取消/确定按钮
     *
     * @param context
     * @param title          对话框的标题
     * @param message        对话框提示的信息
     * @param isCancelable   点击外部是否消失
     * @param dialogCallBack 按钮点击的回调
     */
    public static void showDialog(Context context, String title, String message, boolean isCancelable, DialogCallBack
            dialogCallBack) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancelable)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallBack != null) {
                            dialogCallBack.callBackNegative(dialog);
                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallBack != null) {
                            dialogCallBack.callBackPositive(dialog);
                        }
                    }
                })
                .show();
    }

    /**
     * v7包下 单个按钮的对话框
     *
     * @param context
     * @param title                对话框标题
     * @param message              对话框提示的信息
     * @param isCancelable         点击外部是否可以取消
     * @param dialogSingleCallBack 按钮点击事件的回调
     */
    public static void showSingleButtonDialog(Context context, String title, String message, boolean isCancelable,
                                              DialogSingleCallBack dialogSingleCallBack) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancelable)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogSingleCallBack != null) {
                            dialogSingleCallBack.callBackPositive(dialog);
                        }
                    }
                }).show();
    }

    public static void showEditTextDialog(Context context, String title, String hintMessage, String emptyMessage,
                                          boolean isCancelable, boolean isEmail, DialogInputCallBack dialogInputCallBack) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setPadding(30, 0, 30, 0);

        EditText editText = new EditText(context);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        editText.setBackground(null);
        editText.setHint(hintMessage);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
        frameLayout.addView(editText);
        editText.setLayoutParams(layoutParams);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(frameLayout)
                .setCancelable(isCancelable)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogInputCallBack != null) {
                    if (!isEmail) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            ToastUtils.showShortToast(context, emptyMessage);
                            return;
                        } else {
                            dialogInputCallBack.callBackPositive(alertDialog, editText.getText().toString());
                            alertDialog.dismiss();
                        }
                    } else {
                        if (!DeviceUtil.isEmailNo(editText.getText().toString())) {
                            ToastUtils.showShortToast(context, emptyMessage);
                            return;
                        } else {
                            dialogInputCallBack.callBackPositive(alertDialog, editText.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
    }

    public static void showDatePickerDialog(Context context, DialogInputCallBack dialogInputCallBack) {
        DatePicker datePicker = new DatePicker(context);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(datePicker)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogInputCallBack != null) {
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth() + 1;
                    int day = datePicker.getDayOfMonth();
                    String newMonth="";
                    if (month<10) {
                        newMonth = "0" + month;
                    } else {
                        newMonth = month + "";
                    }
                    StringBuilder stringBuilder = new StringBuilder().append(year).append("-").append(newMonth).append("-")
                            .append
                            (day);
                    dialogInputCallBack.callBackPositive(alertDialog, stringBuilder.toString());
                    alertDialog.dismiss();
                }
            }
        });
    }
    public static void showSexDialog(Context context,int defaultId,DialogInputCallBack dialogInputCallBack){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sex, null);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setNegativeButton("取消", null)
                .show();
        if (defaultId==1) {
            radioGroup.check(R.id.rbMan);
        }else {
            radioGroup.check(R.id.rbWoman);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                LogUtils.e(radioGroup.getCheckedRadioButtonId()+"");
                if (dialogInputCallBack!=null){
                    if (checkedId==R.id.rbMan){
                        dialogInputCallBack.callBackPositive(dialog,"男");
                    }else {
                        dialogInputCallBack.callBackPositive(dialog,"女");
                    }
                    dialog.dismiss();
                }

            }
        });
    }



}

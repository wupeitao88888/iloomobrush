package com.iloomo.utils;


import com.iloomo.paysdk.R;
import com.iloomo.widget.LCDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


// TODO: Auto-generated Javadoc

public class DialogUtil {

    private static LCDialog dialog;

    public static void startDialogLoading(Context context) {
        startDialogLoading(context, true);
    }

    public static void startDialogLoading(Context context, boolean isCancelable) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.progress_dialog,
                null);
        if (dialog == null) {
            dialog = new LCDialog(context, R.style.PgDialog, dialogView);
            if (!isCancelable) {
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);// 设置点击屏幕Dialog不消失
            }
            dialog.show();
        }
    }

    public static void stopDialogLoading(Context context) {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}

package com.jason.blackdoglab.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.jason.blackdoglab.R;

public class DialogUtils{

    public static void showDialog(Context ctx, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.set_dialog);
        builder.setMessage(str);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        showDialogExceptActionBar(builder.create());
    }

    public static void showDialog(Context ctx, int strId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.set_dialog);
        builder.setMessage(strId);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        showDialogExceptActionBar(builder.create());

    }

    public static void showDialogExceptActionBar(AlertDialog alertDialog){
        alertDialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//TODO COLOR
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        if(!alertDialog.isShowing())
            alertDialog.show();
    }

    public static void showDialogExceptActionBar(androidx.appcompat.app.AlertDialog alertDialog) {
        alertDialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//TODO COLOR
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        if(!alertDialog.isShowing())
            alertDialog.show();
    }
}

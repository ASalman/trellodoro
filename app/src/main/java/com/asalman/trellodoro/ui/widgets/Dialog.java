package com.asalman.trellodoro.ui.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.ui.activities.ConfigWizardActivity;

/**
 * Created by asalman on 1/23/16.
 */
public class Dialog {

    private Context mContext;
    private android.app.Dialog mDialog;

    private TextView mDialogText;
    private TextView mDialogOKButton;
    private TextView mDialogCancelButton;
    private static boolean isActive = false;

    public Dialog(Context context) {
        this.mContext = context;
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new android.app.Dialog(mContext,
                    R.style.CustomDialogTheme);
        }
        if (isActive) {
            return;
        }
        mDialog.setContentView(R.layout.dialog);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialog.setCancelable(true);
        mDialog.show();
        isActive = true;

        mDialogText = (TextView) mDialog
                .findViewById(R.id.dialog_universal_warning_text);
        mDialogOKButton = (TextView) mDialog
                .findViewById(R.id.dialog_universal_warning_ok);
        mDialogCancelButton = (TextView) mDialog
                .findViewById(R.id.dialog_universal_warning_cancel);


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isActive = false;
            }
        });
        initDialogButtons();
    }

    private void initDialogButtons() {

        mDialogOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ConfigWizardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                mDialog.dismiss();
            }
        });

        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }
}

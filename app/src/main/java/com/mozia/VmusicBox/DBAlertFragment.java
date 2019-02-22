package com.mozia.VmusicBox;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;

public class DBAlertFragment extends DialogFragment {
    public static final String KEY_ICON = "icon";
    public static final String KEY_ID_DIALOG = "id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NEGATIVE = "negative";
    public static final String KEY_POSITIVE = "positive";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";
    public static final String TAG = DBAlertFragment.class.getSimpleName();
    public static final int TYPE_DIALOG_FULL = 1;
    public static final int TYPE_DIALOG_INFO = 2;

    public static DBAlertFragment newInstance(int idDialog, int iconId, int titleId, int idPositive, int messageId) {
        DBAlertFragment frag = new DBAlertFragment();
        Bundle args = new Bundle();
        args.putInt("type", 2);
        args.putInt(KEY_ID_DIALOG, idDialog);
        args.putInt("title", titleId);
        args.putInt("message", messageId);
        args.putInt(KEY_ICON, iconId);
        args.putInt(KEY_POSITIVE, idPositive);
        frag.setArguments(args);
        return frag;
    }

    public static DBAlertFragment newInstance(int idDialog, int iconId, int titleId, int idPositive, int idNegative, int messageId) {
        DBAlertFragment frag = new DBAlertFragment();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        args.putInt(KEY_ID_DIALOG, idDialog);
        args.putInt("title", titleId);
        args.putInt("message", messageId);
        args.putInt(KEY_ICON, iconId);
        args.putInt(KEY_POSITIVE, idPositive);
        args.putInt(KEY_NEGATIVE, idNegative);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        int type = mBundle.getInt("type");
        int idTitle = mBundle.getInt("title");
        int idMessage = mBundle.getInt("message");
        int idIcon = mBundle.getInt(KEY_ICON);
        int idPositive = mBundle.getInt(KEY_POSITIVE);
        final int idDialog = mBundle.getInt(KEY_ID_DIALOG);
        final DBFragmentActivity mContext = (DBFragmentActivity) getActivity();
        switch (type) {
            case 1:
                return AlertDialogUtils.createFullDialog(mContext, idIcon, idTitle, idPositive, mBundle.getInt(KEY_NEGATIVE), idMessage, new IOnDialogListener() {
                    public void onClickButtonPositive() {
                        mContext.doPositiveClick(idDialog);
                    }

                    public void onClickButtonNegative() {
                        mContext.doNegativeClick(idDialog);
                    }
                });
            case 2:
                return AlertDialogUtils.createInfoDialog(mContext, idIcon, idTitle, idPositive, idMessage, new IOnDialogListener() {
                    public void onClickButtonPositive() {
                        mContext.doPositiveClick(idDialog);
                    }

                    public void onClickButtonNegative() {
                        mContext.doNegativeClick(idDialog);
                    }
                });
            default:
                return super.onCreateDialog(savedInstanceState);
        }
    }
}

package com.scorpions.mcqcorner.config;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.scorpions.mcqcorner.R;

public class Global {
    public static final String FROM_OTP_VERIFICATION = "From OTP Verification";
    public static final String FROM_EMAIL = "From Email";
    public static final String FLAG = "Flag";
    public static final String PROFILE = "Profile";
    public static final String MCQ = "MCQ";
    public static final String USERNAME = "username";
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final CharSequence INVALID = "Invalid";
    public static final CharSequence INVALID_EMAIL = "Invalid Email or Password!";
    public static final CharSequence INVALID_USERNAME = "Invalid Username or Password!";
    public static final CharSequence USERNAME_TAKEN = "Username already taken!";
    public static final String WEBSITE = "website";
    public static final String MOBILE_NO = "mobileNo";
    public static final String PROFILE_PIC = "profilePic";
    public static final String FOLLOWING = "following";
    public static final String TIME = "time";
    public static final String FOLLOWERS = "followers";
    public static final String POSTS = "posts";
    public static final CharSequence USERNAME_SHORT = "Username is too short!";
    public static final String UNFOLLOW = "Unfollow";
    public static final String FOLLOW = "Follow";

    private static OnDialogClickListener clickListener;
    private static Dialog loadingDialog;

    public static void showCustomDialog(OnDialogClickListener listener, Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog.setCancelable(false);

        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        }

        clickListener = listener;

        ((TextView)dialog.findViewById(R.id.lCustomDialog_txtDescription)).setText(msg);

        dialog.findViewById(R.id.lCustomDialog_btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.OnOkClicked();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public interface OnDialogClickListener {
        void OnOkClicked();
    }

    public static void showLoadingDialog(Context context) {
        loadingDialog = new Dialog(context);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.layout_loading);
        loadingDialog.setCancelable(false);

        if(loadingDialog.getWindow()!=null) {
            loadingDialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        }

        loadingDialog.show();
    }

    public static void dismissDialog() {
        loadingDialog.dismiss();
    }
}

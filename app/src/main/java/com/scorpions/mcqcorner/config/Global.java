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
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final CharSequence INVALID = "Invalid";
    public static final CharSequence INVALID_EMAIL = "Invalid Email or Password!";
    public static final CharSequence INVALID_USERNAME = "Invalid Username or Password!";
    public static final CharSequence USERNAME_TAKEN = "Username already taken!";

    private static OnDialogClickListener clickListener;

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
}

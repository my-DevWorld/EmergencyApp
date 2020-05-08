package com.example.emergencyalertapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Essentials {
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    private Date currentDate = new Date();
    private TimeZone timeZone = TimeZone.getDefault();
    private ProgressDialog progressDialog;

    public Essentials() {
    }

    public String getCurrentDate(){
        return dateFormat.format(currentDate);
    }

    public String getTimeZone(){
        return timeZone.getID();
    }

    public void hideSoftKeyboard(Activity activity, View view){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void startProgressLoader(Context context, String msg){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissProgressBar(){
        progressDialog.dismiss();
    }

    public void scrollDown(NestedScrollView scrollView, View view){
        if(view.getVisibility() == View.VISIBLE){
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }
}

package com.demo.drinkdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class AlertDialog extends Dialog {
    private String title;
    private String yesText;
    private String noText;
    private int color;
    private View.OnClickListener btYesListener=null;
    private View.OnClickListener btNoListener=null;

    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_warning);
        LinearLayout background = (LinearLayout) findViewById(R.id.warningBackground);
        background.setBackgroundColor(getBackground());
        TextView title = (TextView) findViewById(R.id.textTitle);
        title.setText(getTitle());
        Button yesButton = (Button) findViewById(R.id.yesButton);
        Button noButton = (Button) findViewById(R.id.noButton);
        yesButton.setText(yesText);
        noButton.setText(noText);
        yesButton.setOnClickListener(btYesListener);
        noButton.setOnClickListener(btNoListener);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPositiveButton(String yes, View.OnClickListener onClickListener) {
        dismiss();
        this.yesText = yes;
        this.btYesListener = onClickListener;
    }

    public void setNegativeButton(String no, View.OnClickListener onClickListener) {
        dismiss();
        this.noText = no;
        this.btNoListener = onClickListener;
    }

    public int getBackground() { return color; }

    public void setBackground(View view){
        if(view.getBackground() instanceof ColorDrawable){
            this.color = ((ColorDrawable) view.getBackground()).getColor();
        }else{
            this.color = ContextCompat.getColor(getContext(),R.color.mainColor);
        }

    }
}

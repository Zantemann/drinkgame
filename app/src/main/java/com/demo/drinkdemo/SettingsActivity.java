package com.demo.drinkdemo;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsActivity extends AppCompatActivity {

    Button language, back;
    SwitchCompat notifications, logout;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        language = findViewById(R.id.languageButton);
        logout = findViewById(R.id.logoutButton);
        notifications = findViewById(R.id.notificationButton);
        back = findViewById(R.id.backButton);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_DENIED){
                notifications.setChecked(true);
            }
        }else{
            notifications.setChecked(true);
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            logout.setText("   Log out");
            logout.setChecked(true);
        }else{
            logout.setText("   Log in");
            logout.setChecked(false);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked){
                if(isChecked){
                    //Notifications
                }else{
                    //No notifications
                }
            }
        });

        logout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    logout.setText("    Log out");
                }else{
                    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    gsc = GoogleSignIn.getClient(SettingsActivity.this, gso);
                    signOut();
                }
            }
        });
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>(){
           @Override
            public void onComplete(Task<Void> task){
               logout.setChecked(false);
               logout.setText("   Log in");
           }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
    }
}
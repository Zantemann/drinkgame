package com.demo.drinkdemo;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

public class LogInActivity extends AppCompatActivity {

    Button questButton, googleButton;
    private static final int REQ_ONE_TAP = 2;

    private boolean showOneTapUI = true;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_log_in_activity);

        questButton = findViewById(R.id.questSignIn);
        googleButton = findViewById(R.id.googleSignIn);

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 101);
            }
        }

        ActiveNotifications();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        // TODO Check right id!
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken !=  null) {
                           String email = credential.getId();
                           Toast.makeText(getApplicationContext(), "Email: "+email, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(LogInActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest =
                                        new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(LogInActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
                                Log.d("TAG", e.getLocalizedMessage());
                            }
                        });
            }
        });

        final AlertDialog loginAlert = new AlertDialog(this);
        loginAlert.setCancelable(false);
        loginAlert.setBackground(findViewById(R.id.logInBackground));
        loginAlert.setTitle("Are you 18 or older?");

        loginAlert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAlert.dismiss();
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAlert.dismiss();
            }
        });

        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAlert.show();
            }
        });
    }

    public void ActiveNotifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        NotificationChannel();
        scheduleNotification(calendar);
    }
    private void NotificationChannel() {
        NotificationChannel channel = new NotificationChannel("Notification","Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void scheduleNotification(Calendar calendar){
        Intent intent = new Intent(getApplicationContext(), NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                100,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog closeAppAlert = new AlertDialog(this);
        closeAppAlert.setBackground(findViewById(R.id.logInBackground));
        closeAppAlert.setTitle("Do you want close the app?");

        closeAppAlert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAppAlert.dismiss();
                finish();
                finishAffinity();
            }
        });

        closeAppAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAppAlert.dismiss();
            }
        });
        closeAppAlert.show();
    }
}
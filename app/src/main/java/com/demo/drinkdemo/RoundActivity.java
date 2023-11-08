package com.demo.drinkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RoundActivity extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        layout = findViewById(R.id.roundActivity);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog endGameAlert = new AlertDialog(this);
        endGameAlert.setBackground(findViewById(R.id.roundBackground));
        endGameAlert.setTitle("Do you want end the game?");

        endGameAlert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGameAlert.dismiss();
                Intent intent = new Intent(RoundActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        endGameAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGameAlert.dismiss();
            }
        });
        endGameAlert.show();
    }
}
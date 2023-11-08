package com.demo.drinkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button add, start, settings;
    LinearLayout layout;
    ArrayList<String> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.playerList);
        add = findViewById(R.id.addButton);
        start = findViewById(R.id.startButton);
        settings = findViewById(R.id.settingsButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleText(view);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Testing if there is players
                if (!players.isEmpty()) {
                    openGameplay();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog closeAppAlert = new AlertDialog(this);
        closeAppAlert.setBackground(findViewById(R.id.container));
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

    public void handleText(View v){
        TextView text = findViewById(R.id.editText);
        String player = text.getText().toString();
        text.setText("");
        //test if there is text
        if(!player.isEmpty()){
            String clearPlayer = player.replaceAll("\\s+", " ");
            clearPlayer = clearPlayer.substring(0,1).toUpperCase() + clearPlayer.substring(1).toLowerCase();
            players.add(clearPlayer);
            addPlayerCard(clearPlayer);
        }
    }

    public void addPlayerCard(String name){
        View view = getLayoutInflater().inflate(R.layout.name_card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.deleteButton);
        nameView.setText(name);

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String deleteName = nameView.getText().toString();
                System.out.print(deleteName);
                layout.removeView(view);
                players.remove(deleteName);
            }
        });
        layout.addView(view);
    }

    public void openGameplay(){
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("players", players);
        startActivity(intent);
    }

    public void openSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
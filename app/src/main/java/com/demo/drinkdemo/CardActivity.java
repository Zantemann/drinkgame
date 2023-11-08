package com.demo.drinkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class CardActivity extends AppCompatActivity {

    Button home;
    LinearLayout layout;
    ArrayList<String> players;
    Integer CARDS;
    Integer ROUNDS = 3;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        home = findViewById(R.id.homeButton);
        layout = findViewById(R.id.card);
        players = getIntent().getStringArrayListExtra("players");
        CARDS = players.size()*ROUNDS;

        //Set the first card
        int startPlayer = getRandomPlayer();
        setCard(startPlayer, 1);

        //home button
        home.setOnClickListener(view -> onBackPressed());

        layout.setOnTouchListener(new View.OnTouchListener() {
            int card = 1;
            int currentRound = 1;
            int playerTurn = startPlayer + 1;
            final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    System.err.println("Click\n");
                    if (card < CARDS) {
                        if (playerTurn >= players.size()) {
                            playerTurn = 0;
                        }

                        if (playerTurn == startPlayer) {
                            currentRound++;
                            openRound();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setCard(startPlayer, currentRound);
                                }
                            }, 500);
                        } else {
                            setCard(playerTurn, currentRound);
                        }
                        card++;
                        playerTurn++;
                    } else {
                        endGame();
                    }
                    return true;
                }

                @Override
                public boolean onDown(MotionEvent event) {
                    System.err.println("onDown\n");
                    return true;
                }
            });
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog endGameAlert = new AlertDialog(this);
        endGameAlert.setBackground(findViewById(R.id.card));
        endGameAlert.setTitle("Do you want end the game?");

        endGameAlert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGameAlert.dismiss();
                finish();
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

    public void setCard(int playerTurn, int currentRound){
        int topicNumber = randomNumber(6) + randomNumber(6);
        getTopic(topicNumber);

        getBackground(topicNumber);

        getPlayer(playerTurn);

        getTask(topicNumber);

        getRound(currentRound);
    }

    public int getRandomPlayer(){
        Random random = new Random();
        int rand;
        while(true){
            rand = random.nextInt(players.size()+1);
            if(rand !=0){
                break;
            }
        }
        return rand -1;
    }

    public void getTopic(int topicNumber){
        TextView topic = (TextView)findViewById(R.id.topic);
        String ok = getString(getResources().getIdentifier("topic_" + topicNumber,"string", getPackageName()));
        topic.setText(ok);
    }

    public void getBackground(int topicNumber){
        LinearLayout background = findViewById(R.id.card);
        String color = getString(getResources().getIdentifier("color_" + topicNumber, "color", getPackageName()));
        background.setBackgroundColor(Color.parseColor(color));
    }

    public void getPlayer(int playerTurn){
        TextView player = (TextView)findViewById(R.id.player);
        player.setText(players.get(playerTurn));
    }

    public void getTask(int topicNumber){
        int maxTask = 6;
        if(topicNumber == 6 || topicNumber == 7 || topicNumber == 8){
            maxTask = 12;
        }

        String text = getString(getResources().getIdentifier("task_" + topicNumber + randomNumber(maxTask), "string", getPackageName()));
        TextView task = (TextView)findViewById(R.id.taskView);
        task.setText(text);
    }

    public void getRound(int round){
        TextView roundView = findViewById(R.id.round);
        String text = "Kierros " + round;
        roundView.setText(text);
    }

    public int randomNumber(int max){
        Random random = new Random();
        int rand;
        while(true){
            rand = random.nextInt(max+1);
            if(rand !=0){
                break;
            }
        }
        return rand;
    }

    public void endGame(){
        finish();
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }

    public void openRound(){
        Intent intent = new Intent(this, RoundActivity.class);
        startActivity(intent);

    }
}
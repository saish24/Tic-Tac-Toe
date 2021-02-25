package com.example.tic_tac_toe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;

public class Show extends AppCompatActivity implements View.OnClickListener {
    private final TextView[][] grid = new TextView [3][3];
    private TextView tv_p1, tv_p2;
    private boolean P1turn = true, resetOpt = false, gameOver = false, multiPlayer, once = true;
    private int roundCount = 0, player1points, player2points;
    private String[][] elements = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiPlayer = getIntent().getBooleanExtra("multiplayer", false);
//        Log.v("TIC TAC TOE", "multiplayer = " + multiplayer);

        tv_p1 = findViewById(R.id.tv_P1);
        tv_p2 = findViewById(R.id.tv_P2);

        if(!multiPlayer) {
//            PLAYING WITH COMPUTER!!
            tv_p2.setText("Computer : " + player2points);
        } else {
            tv_p2.setText("Player 2 : " + player2points);
        }

        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                String buttonid = "mat_" + i + j;
                int resID = getResources().getIdentifier(buttonid, "id", getPackageName());
                grid[i][j] = findViewById(resID);
                grid[i][j].setOnClickListener(this);
            }
        }


        TextView tv_reset = findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(v -> {
            resetOpt = true;
            resetboard();
            player1points = 0;
            player2points = 0;
            updatePoints();
            P1turn = true;
            tv_p1.setTextColor(GREEN);
            tv_p2.setTextColor(WHITE);
        });

    }

    @Override
    public void onClick(View v) {
        if(!gameOver){
            if(!((TextView) v).getText().toString().equals("")) return;
            ++roundCount;

            if(P1turn) {
                ((TextView) v).setText("X");
                ((TextView) v).setTextColor(Color.parseColor("#ff0000"));
                tv_p1.setTextColor(WHITE);
                tv_p2.setTextColor(GREEN);
            } else {
                ((TextView) v).setText("O");
                ((TextView) v).setTextColor(Color.parseColor("#0000ff"));
                tv_p1.setTextColor(GREEN);
                tv_p2.setTextColor(WHITE);
            }


            if (checkforWin()) {
                if(P1turn) player1wins();
                else player2wins();
            } else if(roundCount == 9) {
                draw();
            } else {
                P1turn = !P1turn;
                if(!multiPlayer && once) playBestMove();
            }
        }
    }

    private void player1wins() {
        gameOver = true;
        Toast.makeText(this,"Player 1 wins", Toast.LENGTH_SHORT).show();
        ++player1points;
        resetboard();
        updatePoints();
        P1turn = false;
        tv_p1.setTextColor(WHITE);
        tv_p2.setTextColor(GREEN);

    }

    private void player2wins() {
        gameOver = true;
        if(multiPlayer) Toast.makeText(this,"Player 2 wins", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this,"Computer wins", Toast.LENGTH_SHORT).show();
        ++player2points;
        resetboard();
        updatePoints();
        P1turn = true;
        tv_p1.setTextColor(GREEN);
        tv_p2.setTextColor(WHITE);
    }

    private void updatePoints() {
        String s1 = "Player 1 : " + player1points;
        String s2 = ((multiPlayer) ? "Player 2 : " : "Computer : ") + player2points;
        tv_p1.setText(s1);
        tv_p2.setText(s2);
    }

    private void draw() {
        Toast.makeText(this,"DRAW", Toast.LENGTH_SHORT).show();
        resetboard();
    }

    private void resetboard() {
        if(!resetOpt){
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                for(int i=0; i<3; ++i){
                    for(int j=0; j<3; ++j){
                        grid[i][j].setText("");
                    }
                }
                roundCount = 0;
                gameOver = false;
            }, 600);
        } else {
            resetOpt = false;
            for(int i=0; i<3; ++i){
                for(int j=0; j<3; ++j){
                    grid[i][j].setText("");
                }
            }
            roundCount = 0;
            gameOver = false;
        }
    }

    private boolean checkforWin(){
        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                elements[i][j] = grid[i][j].getText().toString();
            }
        }

        for(int i=0; i<3; ++i){
            if((elements[i][0].equals(elements[i][1]) && elements[i][0].equals(elements[i][2]) && elements[i][0].length() > 0)||
                    (elements[0][i].equals(elements[1][i]) && elements[0][i].equals(elements[2][i]) && elements[0][i].length() > 0)){
                return true;
            }
        }
        return (elements[0][0].equals(elements[1][1]) && elements[0][0].equals(elements[2][2]) && !elements[0][0].equals("")) ||
                (elements[0][2].equals(elements[1][1]) && elements[0][2].equals(elements[2][0]) && !elements[0][2].equals(""));
    }

    void play(int i, int j) {
        grid[i][j].setText("");
        once = false;
        grid[i][j].performClick();
        once = true;
    }

    void playBestMove() {
        if(roundCount == 0 || (roundCount == 1 && grid[1][1].getText().toString().equals("X"))){
            Random rand = new Random();
            int i = rand.nextInt(5);
            if(i == 1) play(0,0);
            else if(i == 2) play(2,0);
            else if(i == 3) play(0, 2);
            else play(2,2);
            return;
        }
        if(roundCount == 1){
            play(1,1);
            return;
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (elements[i][j].equals("")) {
                    grid[i][j].setText("O");
                    if (checkforWin()) {
                        play(i,j);
                        return;
                    }
                    grid[i][j].setText("");
                }
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (elements[i][j].equals("")) {
                    grid[i][j].setText("X");
                    if (checkforWin()) {
                        play(i,j);
                        return;
                    }
                    grid[i][j].setText("");
                }
            }
        }

//      TODO: complete playBestMove function and find the most advantageous click...
        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                if(elements[i][j].equals("")){
                    play(i,j);
                    return;
                }
            }
        }
    }

}
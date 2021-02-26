package com.example.tic_tac_toe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class Show extends AppCompatActivity implements View.OnClickListener {
    private final TextView[][] grid = new TextView [3][3];
    private TextView tv_p1, tv_p2, scrUpdate1, scrUpdate2;
    private boolean humanTurn = true, resetOpt = false, gameOver = false, multiPlayer, once = true;
    private int roundCount = 0, player1points, player2points, pcx, pcy;
    private String aiChar = "O", humanChar = "X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiPlayer = getIntent().getBooleanExtra("multiPlayer", false);

        tv_p1 = findViewById(R.id.tv_P1);
        tv_p2 = findViewById(R.id.tv_P2);
        scrUpdate1 = findViewById(R.id.tv_p1_score);
        scrUpdate2 = findViewById(R.id.tv_p2_score);

        if(!multiPlayer) {
//            PLAYING WITH COMPUTER!!
            String s1 = "Computer : " + player2points;
            tv_p2.setText(s1);
        } else {
            String s2 = "Player 2 : " + player2points;
            tv_p2.setText(s2);
        }

        scrUpdate1.setText(humanChar);
        scrUpdate2.setText(aiChar);

        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                String buttonId = "mat_" + i + j;
                grid[i][j] = findViewById(getResources().getIdentifier(buttonId, "id", getPackageName()));
                grid[i][j].setOnClickListener(this);
            }
        }


        TextView tv_reset = findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(v -> {
            resetOpt = true;
            resetBoard();
            humanChar = "X";
            aiChar = "O";
            player1points = 0;
            player2points = 0;
            humanTurn = true;
            updatePoints();
            tv_p1.setTextColor(GREEN);
            tv_p2.setTextColor(WHITE);
            scrUpdate1.setText(humanChar);
            scrUpdate1.setTextColor(RED);
            scrUpdate2.setText(aiChar);
            scrUpdate2.setTextColor(BLUE);
        });

    }

    @Override
    public void onClick(View v) {
        if(!((TextView) v).getText().toString().equals("")) return;
        if(!gameOver){
            ++roundCount;

            if(humanTurn) {
                ((TextView) v).setText(humanChar);
                ((TextView) v).setTextColor(RED);
                tv_p1.setTextColor(WHITE);
                tv_p2.setTextColor(GREEN);
            } else {
                ((TextView) v).setText(aiChar);
                ((TextView) v).setTextColor(Color.BLUE);
                tv_p1.setTextColor(GREEN);
                tv_p2.setTextColor(WHITE);
            }


            if (checkForWin()) {
                if(humanTurn) player1wins();
                else player2wins();
                humanTurn = true;
                tv_p1.setTextColor(GREEN);
                tv_p2.setTextColor(WHITE);
            } else if(roundCount == 9) {
                draw();
            } else {
                humanTurn = !humanTurn;
                if(!multiPlayer && once) playBestMove();
            }
        }
    }

    private void player1wins() {
        ++player1points;
        gameOver = true;
        Toast.makeText(this,"Player 1 wins", Toast.LENGTH_SHORT).show();

        resetBoard();
        updatePoints();

        String temp = humanChar;
        humanChar = aiChar;
        aiChar = temp;

        scrUpdate1.setText(humanChar);
        scrUpdate2.setText(aiChar);
    }

    private void player2wins() {
        gameOver = true;
        String s = multiPlayer ? "Player 2 wins" : "Computer wins";
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();

        ++player2points;
        resetBoard();
        updatePoints();

        String temp = humanChar;
        humanChar = aiChar;
        aiChar = temp;

        scrUpdate1.setText(humanChar);
        scrUpdate2.setText(aiChar);
    }

    private void updatePoints() {
        String s1 = "Player 1 : " + player1points;
        String s2 = ((multiPlayer) ? "Player 2 : " : "Computer : ") + player2points;
        tv_p1.setText(s1);
        tv_p2.setText(s2);
    }

    private void draw() {
        Toast.makeText(this,"DRAW", Toast.LENGTH_SHORT).show();
        resetBoard();
        humanTurn = true;
        tv_p1.setTextColor(GREEN);
        tv_p2.setTextColor(WHITE);
    }

    private void clearBoard(){
        resetOpt = false;
        roundCount = 0;
        gameOver = false;
        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                grid[i][j].setText("");
            }
        }
    }

    private void resetBoard() {
        if(!resetOpt || (!multiPlayer && !humanTurn)){
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::clearBoard, 300);
        } else clearBoard();
    }

    private boolean checkForWin(){
        String[][] elements = new String[3][3];
        for(int i=0; i<3; ++i)
        for(int j=0; j<3; ++j)
        elements[i][j] = grid[i][j].getText().toString();

        for(int i=0; i<3; ++i)
            if(elements[i][0].equals(elements[i][1]) && elements[i][0].equals(elements[i][2]) && !elements[i][0].equals("")) return true;

        for(int i=0; i<3; ++i)
            if(elements[0][i].equals(elements[1][i]) && elements[0][i].equals(elements[2][i]) && !elements[0][i].equals("")) return true;

        if (elements[0][0].equals(elements[1][1]) && elements[0][0].equals(elements[2][2]) && !elements[0][0].equals("")) return  true;

        return elements[0][2].equals(elements[1][1]) && elements[0][2].equals(elements[2][0]) && !elements[0][2].equals("");
    }

    void play(int i, int j) {
        grid[i][j].setText("");
        once = false;
        grid[i][j].performClick();
        once = true;
    }

    Pair<Integer, Integer> minimax(int x, int y, boolean isMaximizing, int depth){
        if(checkForWin()) {
            return new Pair<>(isMaximizing ? -1 : 1, depth-1);
        } else if(roundCount == 9) {
            return new Pair<>(0, depth-1);
        } else if(isMaximizing){
            int maxVal = -10, minDepth = Integer.MAX_VALUE;
            grid[x][y].setText(aiChar);
            ++roundCount;

            for(int i=0; i<3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (grid[i][j].getText().toString().equals("")) {
                        Pair<Integer, Integer> currscore = minimax(i, j, false, depth+1);

                          if(maxVal < currscore.first) {
                            maxVal = currscore.first;
                            minDepth = currscore.second;
                        }
                        if(maxVal == currscore.first && minDepth > currscore.second) minDepth = currscore.second;

                    }
                }
            }

            --roundCount;
            grid[x][y].setText("");
            return new Pair<>(maxVal, minDepth);
        } else {
            int minVal = 10, minDepth = Integer.MAX_VALUE;
            grid[x][y].setText(humanChar);
            ++roundCount;

            for(int i=0; i<3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (grid[i][j].getText().toString().equals("")) {
                        Pair<Integer, Integer> currscore = minimax(i, j, true, depth+1);

                        if(minVal > currscore.first) {
                            minVal = currscore.first;
                            minDepth = currscore.second;
                        }

                        if(minVal == currscore.first && minDepth > currscore.second) minDepth = currscore.second;

                    }
                }
            }

            grid[x][y].setText("");
            --roundCount;
            return new Pair<>(minVal, minDepth);
        }
    }

    boolean searchGrid(String c){
        boolean winner = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (grid[i][j].getText().toString().equals("")) {
                    grid[i][j].setText(c);
                    if (checkForWin()) {
                        pcx = i;
                        pcy = j;
                        winner = true;
                    }
                    grid[i][j].setText("");
                }
            }
        }
        return winner;
    }

    void playBestMove() {
//       1st move is Corner move
        if(roundCount == 0) play(0,0);

//        else if(roundCount == 1 && grid[1][1].getText().toString().equals("")) play(1, 1);

//      We first check for a win
        else if(searchGrid(aiChar)) play(pcx, pcy);

//      We ensure opponent does not have a single move win
        else if(searchGrid(humanChar)) play(pcx, pcy);

//      apply backtracking algorithm - minimax
        else if(roundCount < 9) {
            int maxScore = -1, posx = -1, posy = -1, depthval = Integer.MAX_VALUE;
            for(int i=0; i<3; ++i){
                for(int j=0; j<3; ++j){
                    if(grid[i][j].getText().toString().equals("")){

                        Pair<Integer, Integer> currScore = minimax(i, j, true, 0);
                        if(maxScore < currScore.first){
                            posx = i;
                            posy = j;
                            maxScore = currScore.first;
                            depthval = currScore.second;
                        }
                        if(maxScore == currScore.first && depthval > currScore.second){
                            posx = i;
                            posy = j;
                            depthval = currScore.second;
                        }

                    }
                }
            }
            play(posx, posy);
        }
    }
}

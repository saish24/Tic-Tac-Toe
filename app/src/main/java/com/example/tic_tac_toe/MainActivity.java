package com.example.tic_tac_toe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final TextView[][] grid = new TextView [3][3];
    private TextView tv_p1, tv_p2;
    private boolean P1turn = true, resetOpt = false;
    private int roundCount = 0, player1points, player2points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_p1 = findViewById(R.id.tv_P1);
        tv_p2 = findViewById(R.id.tv_P2);

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
        if(!((TextView) v).getText().toString().equals("")) return;
        if(P1turn) {
            ((TextView) v).setText("X");
            ((TextView) v).setTextColor(Color.parseColor("#ff0000"));
        } else {
            ((TextView) v).setText("O");
            ((TextView) v).setTextColor(Color.parseColor("#0000ff"));
        }
        ++roundCount;
        if(P1turn){
            tv_p2.setTextColor(GREEN);
            tv_p1.setTextColor(WHITE);
        } else {
            tv_p1.setTextColor(GREEN);
            tv_p2.setTextColor(WHITE);
        }
        P1turn = !P1turn;
        if(roundCount == 9) draw();
        else if (checkforWin()) {
            if(!P1turn) player1wins();
            else player2wins();
        }
    }

    private void player1wins() {
        Toast.makeText(this,"Player 1 wins", Toast.LENGTH_SHORT).show();
        ++player1points;
        resetboard();
        updatePoints();
    }
    private void player2wins() {
        Toast.makeText(this,"Player 2 wins", Toast.LENGTH_SHORT).show();
        ++player2points;
        resetboard();
        updatePoints();
    }

    private void updatePoints() {
        String s1 = "Player 1 : " + player1points;
        String s2 = "Player 2 : " + player2points;

        tv_p1.setText(s1);
        tv_p2.setText(s2);
    }

    private boolean checkforWin(){
        String[][] elements = new String[3][3];
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
        return (elements[0][0].equals(elements[1][1]) && elements[0][0].equals(elements[2][2]) && elements[0][0].length() > 0) ||
                (elements[0][2].equals(elements[1][1]) && elements[0][2].equals(elements[2][0]) && elements[0][2].length() > 0);
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
            }, 600);
        } else {
            resetOpt = false;
            for(int i=0; i<3; ++i){
                for(int j=0; j<3; ++j){
                    grid[i][j].setText("");
                }
            }
        }
        roundCount = 0;
    }
}
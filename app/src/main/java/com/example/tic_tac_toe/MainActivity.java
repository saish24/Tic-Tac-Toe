package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView[][] grid = new TextView [3][3];
    private TextView tv_p1, tv_p2;
    private boolean P1turn = true;
    private int roundCount = 0, player1points, player2points;
    private String green = "#64DD17", black = "#ffffff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_p1 = (TextView) findViewById(R.id.tv_P1);
        tv_p2 = (TextView) findViewById(R.id.tv_P2);

        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                String buttonid = "mat_" + i + j;
                int resID = getResources().getIdentifier(buttonid, "id", getPackageName());
                grid[i][j] = (TextView) findViewById(resID);
                grid[i][j].setOnClickListener(this);
            }
        }

        TextView tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1points = 0;
                player2points = 0;
                updatePoints();
                resetboard();
                roundCount = 0;
                tv_p2.setTextColor(Color.parseColor(green));
                tv_p1.setTextColor(Color.parseColor(black));
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(!((TextView) v).getText().toString().equals("")){
            return;
        }
        if(P1turn) {
            ((TextView) v).setText("X");
        } else {
            ((TextView) v).setText("O");
        }
        ++roundCount;
        if (checkforWin()) {
            if(P1turn) player1wins();
            else player2wins();
        }
        if(P1turn){
            tv_p2.setTextColor(Color.parseColor(green));
            tv_p1.setTextColor(Color.parseColor(black));
        } else {
            tv_p1.setTextColor(Color.parseColor(green));
            tv_p2.setTextColor(Color.parseColor(black));
        }

        P1turn = !P1turn;

    }

    private void player1wins() {
        Toast.makeText(this,"Player 1 wins", Toast.LENGTH_SHORT).show();
        ++player1points;
        updatePoints();
        resetboard();
    }
    private void player2wins() {
        Toast.makeText(this,"Player 2 wins", Toast.LENGTH_SHORT).show();
        ++player2points;
        updatePoints();
        resetboard();
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
        if((elements[0][0].equals(elements[1][1]) && elements[0][0].equals(elements[2][2]) && elements[0][0].length() > 0) ||
                (elements[0][2].equals(elements[1][1]) && elements[0][2].equals(elements[2][0]) && elements[0][2].length() > 0)){
            return true;
        }
        if(roundCount == 9) draw();
        return false;
    }

    private void draw() {
        Toast.makeText(this,"DRAW", Toast.LENGTH_SHORT).show();
        resetboard();
    }

    private void resetboard() {
        for(int i=0; i<3; ++i){
            for(int j=0; j<3; ++j){
                grid[i][j].setText("");
            }
        }
        roundCount = 0;
    }
}
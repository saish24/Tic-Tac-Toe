package com.example.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode);

        TextView tv_1 = findViewById(R.id.tv_single_player);
        TextView tv_2 = findViewById(R.id.tv_multi_player);

        tv_1.setOnClickListener(v -> {
//            PLAYING WITH COMPUTER!!
            Intent intent = new Intent(this, Show.class);
            intent.putExtra("multiplayer", false);
            startActivity(intent);
        });
        tv_2.setOnClickListener(v -> {
//            PLAYING WITH HUMAN!!
            Intent intent = new Intent(this, Show.class);
            intent.putExtra("multiplayer", true);
            startActivity(intent);
        });
    }
}
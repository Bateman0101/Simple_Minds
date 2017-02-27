package com.example.manuel.sudo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;
import static java.security.AccessController.getContext;

public class Scores extends AppCompatActivity {

    TextView txt1;
    TextView txt2;
    TextView txt3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int gamesPlayed=pref.getInt("GAMES_PLAYED", 0);
        int gamesWon=pref.getInt("GAMES_WON",0);
        float gameTime=pref.getFloat("GAME_TIME", 0);
        gamesPlayed++;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("GAMES_PLAYED", gamesPlayed);
        editor.apply();

        txt1 = (TextView)findViewById(R.id.id_partite);
        txt1.setText("              "+String.valueOf(gamesPlayed));

        txt2 = (TextView)findViewById(R.id.partite_vinte);
        txt2.setText("               "+String.valueOf(gamesWon));

        txt3 = (TextView)findViewById(R.id.tempo1);
        if (gameTime==0)  txt3.setText(" //");
        else txt3.setText("             "+String.valueOf(gameTime)+" min");

    }



}



package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

    TextView menu;
    TextView list;
    Button picture;
    Button pay;
    Button vote;
    Button dice;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        menu = (TextView) findViewById(R.id.menu);
        list = (TextView) findViewById(R.id.list);
        picture = (Button) findViewById(R.id.btn_picture);
        pay= (Button) findViewById(R.id.btn_pay);
        vote = (Button) findViewById(R.id.btn_vote);
        dice = (Button) findViewById(R.id.btn_dice);
        close = (Button) findViewById(R.id.btn_close);




    }

}
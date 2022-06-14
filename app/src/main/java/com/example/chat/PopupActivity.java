package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopupActivity extends AppCompatActivity {

    String CHAT_NAME;
    String USER_NAME;
    TextView menu;
    ListView list;
    Button picture;
    Button pay;
    Button vote;
    Button dice;
    Button close;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        menu = (TextView) findViewById(R.id.menu);
        list = (ListView) findViewById(R.id.chat_list);
        picture = (Button) findViewById(R.id.btn_picture);
        pay= (Button) findViewById(R.id.btn_pay);
        vote = (Button) findViewById(R.id.btn_vote);
        dice = (Button) findViewById(R.id.btn_dice);
        close = (Button) findViewById(R.id.btn_close);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent close = new Intent(PopupActivity.this, ChatActivity.class);
                startActivity(close);
            }
        });





    }


}
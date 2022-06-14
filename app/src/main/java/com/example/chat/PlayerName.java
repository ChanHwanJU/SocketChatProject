package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayerName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);

        final EditText playerNameEt = findViewById(R.id.enterName);
        final AppCompatButton startGameBtn = findViewById(R.id.startGameBtn);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting player name from EditText to a string variable
                final String getPlayerName = playerNameEt.getText().toString();

                // checking if player has entered his name
                if(getPlayerName.isEmpty()){
                    Toast.makeText(PlayerName.this, "Please Enter Player Name ", Toast.LENGTH_SHORT ).show();
                }
                else{
                    // creating intent to open UserInformationActivity
                    Intent intent = new Intent(PlayerName.this, UserInfoActivity.class);

                    // adding Player name along with intent
                    intent.putExtra("PlayerName", getPlayerName);

                    // opening UserInformationActivity
                    startActivity(intent);

                    // To destroy this activity
                    finish();
                }
            }
        });
    }
}
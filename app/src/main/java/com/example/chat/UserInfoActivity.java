package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserInfoActivity extends AppCompatActivity {

    private LinearLayout player1layout, player2layout;
    private Button btn;
    private TextView player1Tv, player2Tv, playerResult;

    // viewing combinations
    private final List<int[]> combinationsList = new ArrayList<>();
    private final List<String> doneBoxes = new ArrayList<>(); // done boxes positions by users so users won't select the box again

    // players unique id
    private String player_uniqueId = "0";

    // getting firebase database reference from url
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dice-51e26-default-rtdb.firebaseio.com/");

    // true when opponent will be found to play the game
    private boolean opponentFound = true;

    // unique id of opponent
    private String opponent_uniqueId = "0";

    // values must be matching or waiting when a user create a new connection room and he is waiting for other to join then the value will be waiting
    private String status = "matching";

    // player turn
    private String playerTurn = "";

    // connection id in which player has joined to play the game
    private String connectionId = "";

    // Generating ValueEventListeners for Firebase Database
    // turnsEventListener listen for the players turns and wonEventListener listen if the player has won the match
    ValueEventListener turnsEventListener, wonEventListener;

    // selected boxes by players. empty field will be replaced by player ids
    private final String[] boxesSelectedBy = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        player1layout = findViewById(R.id.player1layout);
        player2layout = findViewById(R.id.player2layout);

        btn = findViewById(R.id.dice_button);

        player1Tv = findViewById(R.id.player1TV);
        player2Tv = findViewById(R.id.player2TV);
        playerResult = findViewById(R.id.textViewResult);

        // getting PlayerName from PlayerName.class file
        final String getPlayerName = getIntent().getStringExtra("PlayerName");

        // generating winning combinations
        combinationsList.add(new int[]{1});
        combinationsList.add(new int[]{2});
        combinationsList.add(new int[]{3});
        combinationsList.add(new int[]{4});
        combinationsList.add(new int[]{5});
        combinationsList.add(new int[]{6});

        // showing progress dialog while waiting for opponent
        /*ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting For Opponent");
        progressDialog.show();*/

        // generate player unique id, Player will be identified by this id.
        player_uniqueId = String.valueOf(System.currentTimeMillis());

        // setting player name to the TextView
        player1Tv.setText(getPlayerName);
        //player2Tv.setText(getPlayerName);

        // connection id in which player has joined to play the game
        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // check if opponent found or not if not then look for the opponent
                if (opponentFound) {

                    // checking if there are others in the firebase realtime database
                    if (snapshot.hasChildren()) {

                        // checking all connections if other users are also waiting for a user to match
                        for (DataSnapshot connections : snapshot.getChildren()){

                            // getting connection unique id
                            String conId = (connections.getKey());

                            // 2 players are required to play the game
                            // if getPlayersCount is 1 it means other player is waiting for a opponent to play the game
                            // else if get players count is 2 it means this connection has completed with 2 players
                            int getPlayersCount = (int)connections.getChildrenCount();


                            // after created a new connection waiting for other to join
                            if(status.equals("waiting")){

                                // if get_playersCount is 2 means other player joined the match
                                if(getPlayersCount == 2){

                                    playerTurn = player_uniqueId;
                                    applyPlayerTurn(playerTurn);

                                    // true when player found in connection
                                    boolean playerFound = false;

                                    // getting player is connection
                                    for (DataSnapshot players: connections.getChildren()){

                                        String getPlayerUniqueId = players.getKey();

                                        // check if player id match with user who created connection(this user). if match them get opponent details
                                        if (getPlayerUniqueId.equals(player_uniqueId)){
                                            playerFound = true;
                                        }
                                        else if (playerFound){

                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            opponent_uniqueId = players.getKey();

                                            // set opponent playerName to the TextView
                                            player2Tv.setText(getOpponentPlayerName);

                                            // assigning connection id
                                            connectionId = conId;
                                            opponentFound = true;

                                            // adding turns listener and won listener to the database reference
                                            databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                            databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);

                                            // hide progress dialog if showing
                                            /*if (progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }*/

                                            // once the connection has made remove connectionListener from Database reference
                                            databaseReference.child("connections").removeEventListener(this);
                                        }

                                    }

                                }

                            }
                            // in case user has not created the connection room because of other rooms are available to join
                            else {

                                // checking if the connection has 1 player and need 1 more player to play the match the join this connection
                                if (getPlayersCount == 1){

                                    // add player to the connection
                                    connections.child(player_uniqueId).child("player_name").getRef().setValue(getPlayerName);

                                    // getting both players
                                    for (DataSnapshot players : connections.getChildren()){

                                        String getOpponentName = players.child("player_name").getValue(String.class);
                                        opponent_uniqueId = players.getKey();

                                        // first turn will be of who created the connection / room
                                        playerTurn = opponent_uniqueId;
                                        applyPlayerTurn(playerTurn);

                                        // setting playerName to the textView
                                        player2Tv.setText(getOpponentName);

                                        // assigning connection id
                                        connectionId = conId;
                                        opponentFound = true;

                                        // adding turns listener and won listener to the database reference
                                        databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                        databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);


                                        // hide progress dialog if showing
                                        /*if (progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }*/

                                        // once the connection has made remove connectionListener from Database reference
                                        databaseReference.child("connections").removeEventListener(this);

                                        break;
                                    }

                                }
                            }

                        }

                        // check if opponent is not found and other is not waiting for the opponent anymore then create a new connection
                        if (!opponentFound && !status.equals("waiting")){

                            // generating  unique id for the connection
                            String connection_uniqueId = String.valueOf(System.currentTimeMillis());

                            // adding first player to the connection and waiting for other to complete the connection and play the game
                            snapshot.child(connection_uniqueId).child(player_uniqueId).child("player_name").getRef().setValue(getPlayerName);

                            status = "waiting";

                        }
                    }

                    // if there is no connection available in the firebase database then create a new connection.
                    // it is like creating a room and waiting for other players to join the room
                    else {

                        // generating  unique id for the connection
                        String connection_uniqueId = String.valueOf(System.currentTimeMillis());

                        // adding first player to the connection and waiting for other to complete the connection and play the game
                        snapshot.child(connection_uniqueId).child(player_uniqueId).child("player_name").getRef().setValue(getPlayerName);

                        status = "waiting";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        // don't know what here yet
        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // getting all turns of the connection
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if (dataSnapshot.getChildrenCount() == 2){

                        // getting box position selected by the user
                        final int getBoxPosition = Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));

                        // getting player id who selected the box
                        final String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);


                        // checking if user has not selected the box before
                        if (doneBoxes.contains(String.valueOf(getBoxPosition))){

                            // select the box
                            doneBoxes.add(String.valueOf(getBoxPosition));

                            if (getBoxPosition == 1){
                                selectBox(btn, getBoxPosition, getPlayerId);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Random random = new  Random();
                                        int val = random.nextInt(7-1)+1;
                                        playerResult.setText(Integer.toString(val));
                                    }
                                });
                            }

                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // check if a user has won the match
                if (snapshot.hasChild("player_id")){

                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);

                    final WinDialog winDialog;

                    if (getWinPlayerId.equals(player_uniqueId)){

                        // SHOW WIN DIALOG
                        winDialog = new WinDialog(UserInfoActivity.this, "You won the game");
                    }
                    else {
                        // SHOW WIN DIALOG
                        winDialog = new WinDialog(UserInfoActivity.this, "Opponent won the game");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

//        if (getPlayerName.equals(player_uniqueId)){
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Random random = new Random();
//                    int val = random.nextInt(7-1)+1;
//                    playerResult.setText(Integer.toString(val));
//                }
//            });
//
//            playerTurn = opponent_uniqueId;
//            applyPlayerTurn(playerTurn);
//        }
    }

    // to be changing player turn to play the game
    private void applyPlayerTurn(String playerUniqueId){

        if (playerUniqueId.equals(player_uniqueId)){
            player1layout.setBackgroundResource(R.drawable.round_blue_dark);
            player2layout.setBackgroundResource(R.drawable.round_blue_dark_2);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Random random = new  Random();
                    int val = random.nextInt(7-1)+1;
                    playerResult.setText(Integer.toString(val));
                }
            });

        }
        else{
            player2layout.setBackgroundResource(R.drawable.round_blue_dark);
            player1layout.setBackgroundResource(R.drawable.round_blue_dark_2);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Random random = new  Random();
                    int val = random.nextInt(7-1)+1;
                    playerResult.setText(Integer.toString(val));
                }
            });

        }
        playerTurn = opponent_uniqueId;
    }

    private void selectBox(Button button, int selectedBoxPosition, String selectedByPlayer){

        boxesSelectedBy[selectedBoxPosition -1] = selectedByPlayer;

        if (selectedByPlayer.equals(player_uniqueId)){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Random random = new  Random();
                    int val = random.nextInt(7-1)+1;
                    playerResult.setText(Integer.toString(val));
                }
            });

            playerTurn = opponent_uniqueId;
        }

        applyPlayerTurn(playerTurn);

        // checking whether player has won the match
        if (checkPlayerWin(selectedByPlayer)){

            // sending won player unique id to firebase won opponent can be notified
            databaseReference.child("won").child(connectionId).child("player_id").setValue(selectedByPlayer);
        }
        // over the game if there is no box left to be selected
        if (doneBoxes.size() == 7){
            final WinDialog winDialog = new WinDialog(UserInfoActivity.this, "it is a Draw!");
            winDialog.setCancelable(false);
            winDialog.show();
        }
    }

    private boolean checkPlayerWin( String playerId){
        boolean isPlayerWon = false;

        // compare player turns with every wining combination
        for (int i = 0; i < combinationsList.size(); i++){

            final int[] combination = combinationsList.get(i);

            if (boxesSelectedBy[combination[0]].equals(playerId)){

                isPlayerWon = true;
            }
        }

        return isPlayerWon;
    }
}
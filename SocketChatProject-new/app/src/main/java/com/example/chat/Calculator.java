package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {

    private EditText TotalM;
    private EditText TotalH;
    private Button ok_btn;
    private Button cancel_btn;
    private String T_String;
    private String N_String;
    private TextView view_result;

    int m = 0;
    int h = 0;
    int result = 0;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TotalM = (EditText) findViewById(R.id.TotalM);
        TotalH = (EditText) findViewById(R.id.TotalH);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        view_result = (TextView) findViewById(R.id.view_result);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = TotalM.getText().toString();
                String human = TotalH.getText().toString();


                m = Integer.valueOf(money);
                h = Integer.valueOf(human);

                result = m / h;
                n = m % h;

                T_String = "내셔야할 금액은 " + String.valueOf(result) + "원 입니다.";
                N_String = "한분만 " + String.valueOf(n) + "원 추가로 내시면 됩니다.";


                view_result.setText(T_String + "\n" + N_String);



            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Calculator.this, PopupActivity.class);
                startActivity(in);

            }
        });
    }
}
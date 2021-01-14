package com.example.emailactivation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.R;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.User;

import org.json.JSONException;

public class SignUpActivity extends AppCompatActivity {
    public static final String APPLICANT="applicant";
    EditText etUsername,etPassword;
    Button btnSignUp,btnBack;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsername=findViewById(R.id.et_username);
        etPassword=findViewById(R.id.et_password);
        btnSignUp=findViewById(R.id.btn_signUp);
        btnBack=findViewById(R.id.btn_back);

        databaseHelper=new DatabaseHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
                {
                    String username=etUsername.getText().toString().trim();
                    String password=etPassword.getText().toString().trim();
                    User authenticate=databaseHelper.Authenticate(username);
                    if(authenticate==null){
                        try {
                            ((MyApplication) getApplication()).getUserByUsername(username,password,APPLICANT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "FIll all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
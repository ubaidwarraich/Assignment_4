package com.example.emailactivation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.R;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.User;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    public static final String CORDINATER = "cordinater";
    public static final String HOD = "hod";
    public static final String APPLICANT="applicant";

    Spinner spinner;
    Button btnLogin,btnSignUp;
    EditText etUsername,etPassword;
    String selectedRole=APPLICANT;
    DatabaseHelper databaseHelper=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        if(databaseHelper.Authenticate(HOD)==null) {
            try {
                ((MyApplication) getApplication()).createAdminAccountsOnServer(HOD, CORDINATER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btnLogin=findViewById(R.id.btn_login);
        btnSignUp=findViewById(R.id.btn_signUp);
        etUsername=findViewById(R.id.et_username);
        etPassword=findViewById(R.id.et_password);
        spinner= findViewById(R.id.spinner);


        String[] spinnerList=new String[]{"Applicant","HOD","Co-ordinator"};
        ArrayAdapter<String> adapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
                {
                    String username=etUsername.getText().toString().trim();
                    String password=etPassword.getText().toString().trim();
                        User s = databaseHelper.Authenticate(username);
                        if(s==null){
                            try {
                                ((MyApplication) getApplication()).getUserByUsername(username,password,selectedRole);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(!(etUsername.getText().toString().equals(etPassword.getText().toString()))){
                            try {
                                ((MyApplication) getApplication()).getUserByUsername(username,password,APPLICANT);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if((etUsername.getText().toString().equals(etPassword.getText().toString()))&& selectedRole==HOD)
                    {
                        try {
                            ((MyApplication) getApplication()).getUserByUsername(username,password,HOD);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                        else if((etUsername.getText().toString().equals(etPassword.getText().toString()))&& selectedRole==CORDINATER)
                        {
                            try {
                                ((MyApplication) getApplication()).getUserByUsername(username,password,CORDINATER);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(s!=null) {
                            if (s.getUsername().equals(username) && s.getPassword().equals(password) && s.getRole().equals(selectedRole)) {
                                ((MyApplication) getApplication()).setUserActive(s);
                                if (selectedRole == APPLICANT) {
                                    Log.i("global user:", "onClick: "+((MyApplication) getApplication()).getUserActive().getUsername());
                                    Intent intent = new Intent(MainActivity.this, StudentDashboard.class);
                                    startActivity(intent);
                                } else if (selectedRole == HOD) {
                                    Intent intent=new Intent(MainActivity.this,HodDashboardActivity.class);
                                    startActivity(intent);
                                    return;
                                } else if (selectedRole == CORDINATER) {
                                    Intent intent=new Intent(MainActivity.this,AdminDashboardActivity.class);
                                    startActivity(intent);
                                    return;
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                }
                else {
                    Toast.makeText(MainActivity.this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    selectedRole=APPLICANT;
                }
                if(position == 1)
                {
                    selectedRole=HOD;
                }
                if(position == 2)
                {
                    selectedRole=CORDINATER;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    
}
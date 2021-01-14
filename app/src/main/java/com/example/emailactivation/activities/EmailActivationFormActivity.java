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

import java.sql.DatabaseMetaData;

public class EmailActivationFormActivity extends AppCompatActivity {
    User user;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_activation_form);
        user=((MyApplication) getApplication()).getUserActive();
        databaseHelper=new DatabaseHelper(this);
        setTitle("New Form");
        EditText nameText=findViewById(R.id.NameTextBox);
        EditText rollNumberText=findViewById(R.id.RollNumberTextBox);
        EditText semesterText=findViewById(R.id.SemesterTextBox);
        EditText cnicText=findViewById(R.id.CnicTextBox);
        EditText messageText=findViewById(R.id.messageTextBox);
        Button btnSubmit=findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=nameText.getText().toString();
                String rollNumber=rollNumberText.getText().toString();
                String semester=semesterText.getText().toString();
                String cnic=cnicText.getText().toString();
                String message=messageText.getText().toString();

                if(Name.isEmpty()){
                    nameText.setError("This field cannot be empty");
                }
                if(rollNumber.isEmpty()){
                    rollNumberText.setError("This filed canot be empty");
                }
                if(semester.isEmpty()){
                    semesterText.setError("This filed cannot be empty");
                }
                if(cnic.isEmpty()){
                    cnicText.setError("This filed cannot be empty");
                }
                boolean b = databaseHelper.checkFormApproval(user.getId());
                Log.i("forms count", "onClick: "+b);
                if(b){
                    Intent intent=new Intent();
                    intent.putExtra("name",Name);
                    intent.putExtra("rollNO", rollNumber);
                    intent.putExtra("semester", semester);
                    intent.putExtra("cnic", cnic);
                    intent.putExtra("message",message);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else
                {
                    Toast.makeText(EmailActivationFormActivity.this, "You can process only one application at a time.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EmailActivationFormActivity.this,StudentDashboard.class);
                    startActivity(intent);
                }


            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act=new Intent();
        setResult(RESULT_CANCELED,act);
        finish();
    }
}
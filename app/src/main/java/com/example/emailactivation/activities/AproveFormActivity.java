package com.example.emailactivation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.R;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.models.Form;
import com.example.emailactivation.models.User;

import org.json.JSONException;

public class AproveFormActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView tv_studentName,tv_semester,tv_rollno,tv_cnic,tv_message;
    EditText et_comments;
    Button btn_approve,btn_disaprove,btn_SendtoHod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprove_form);
        setTitle("Approve Form");
        databaseHelper=new DatabaseHelper(this);
        tv_studentName=findViewById(R.id.tv_studentname);
        tv_semester=findViewById(R.id.tv_semester);
        tv_rollno=findViewById(R.id.tv_rollno);
        tv_cnic=findViewById(R.id.tv_Cnic);
        tv_message=findViewById(R.id.tv_Message);
        et_comments=findViewById(R.id.et_comments);
        btn_approve=findViewById(R.id.btn_approve);
        btn_disaprove=findViewById(R.id.btn_disaprove);
        btn_SendtoHod=findViewById(R.id.btn_sendToHod);

        int position = getIntent().getExtras().getInt("position");
        Applicant applicant=((MyApplication) getApplication()).getApplicantById(position);
        Form form=((MyApplication) getApplication()).getFormByApplicantId(applicant.getId());


        tv_studentName.setText(applicant.getName());
        tv_semester.setText(applicant.getSemester());
        tv_cnic.setText(applicant.getCnic());
        tv_rollno.setText(applicant.getRollNo());
        tv_message.setText(form.getMessage());
        et_comments.setText(form.getComments());

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userActive = ((MyApplication) getApplication()).getUserActive();

                String com=et_comments.getText().toString();
                databaseHelper.approveForm(form.getId(),userActive.getRole(),com);
                form.setApproved(1);
                form.setComments(com);
                form.setApprovedBy(userActive.getRole());

                try {
                    ((MyApplication) getApplication()).updateForm(form);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ReturnToAdminActivity();
            }
        });

        btn_disaprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userActive = ((MyApplication) getApplication()).getUserActive();
                databaseHelper.disapproveForm(form.getId(),userActive.getRole(),et_comments.getText().toString());
                form.setApproved(0);
                form.setComments(et_comments.getText().toString());
                form.setApprovedBy(userActive.getRole());

                try {
                    ((MyApplication) getApplication()).updateForm(form);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ReturnToAdminActivity();

            }
        });

        btn_SendtoHod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userActive = ((MyApplication) getApplication()).getUserActive();
                databaseHelper.sendToHod(form.getId(),userActive.getRole(),et_comments.getText().toString());
                form.setApproved(0);
                form.setComments(et_comments.getText().toString());
                form.setApprovedBy("pending");

                try {
                    ((MyApplication) getApplication()).updateForm(form);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ReturnToAdminActivity();

            }
        });
    }

    private void ReturnToAdminActivity() {
        Intent intent=new Intent(AproveFormActivity.this,AdminDashboardActivity.class);
        startActivity(intent);
    }
}
package com.example.emailactivation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.R;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.models.Form;

public class ViewApplicationStatus extends AppCompatActivity {
    TextView tv_name,tv_roll,tv_semester,tv_cnic,tv_message,tv_comment;
    ImageView iconApproved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application_status);

        tv_name=findViewById(R.id.tv_studentname);
        tv_roll=findViewById(R.id.tv_rollno);
        tv_semester=findViewById(R.id.tv_semester);
        tv_cnic=findViewById(R.id.tv_Cnic);
        tv_message=findViewById(R.id.tv_Message);
        tv_comment=findViewById(R.id.tv_comments);
        iconApproved=findViewById(R.id.ic_approved);

        int formNo=getIntent().getExtras().getInt("position");
        Form form=   ((MyApplication) getApplication()).getForm(formNo);
        Applicant applicant= ((MyApplication) getApplication()).getApplicant();
       tv_name.setText(applicant.getName());
       tv_roll.setText(applicant.getRollNo());
       tv_semester.setText(applicant.getSemester());
       tv_cnic.setText(applicant.getCnic());
       tv_message.setText(form.getMessage());
       tv_comment.setText(form.getComments());
       if(form.isApproved()==0) {
           iconApproved.setImageResource(R.drawable.wrong);
       }
       else{
           iconApproved.setImageResource(R.drawable.check);
       }
    }
}
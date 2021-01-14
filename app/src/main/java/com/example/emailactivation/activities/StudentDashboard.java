package com.example.emailactivation.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.adapters.ApplicantListAdapter;
import com.example.emailactivation.R;
import com.example.emailactivation.models.Form;
import com.example.emailactivation.models.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends AppCompatActivity {
    User user;
    DatabaseHelper databaseHelper;
    List<Applicant> studentsList=new ArrayList<Applicant>();
    public static int formsCount=0;
    public static final int form_Request=01;
    ApplicantListAdapter ListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=((MyApplication) getApplication()).getUserActive();
        databaseHelper = new DatabaseHelper(this);
        setTitle("Student Dashboard");
        ListAdapter=new ApplicantListAdapter(studentsList, StudentDashboard.this);
        ListView lst=findViewById(R.id.lst);
        lst.setAdapter(ListAdapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(StudentDashboard.this,ViewApplicationStatus.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        showApplicantForms();
    }

    private void showApplicantForms() {
        Applicant applicant = databaseHelper.getApplicant(user.getId());
        if(applicant!=null){
            ((MyApplication) getApplication()).setApplicant(applicant);
            List<Form> forms = databaseHelper.getForms(applicant.getId());
           if(forms.size()>0){
               ((MyApplication) getApplication()).setForms(forms);
               for (Form f:forms) {
                   this.studentsList.add(applicant);
               }
           }
        }
        ListAdapter.notifyDataSetChanged();
    }

    public void openNewForm(View view) {
        Intent intent=new Intent(this, EmailActivationFormActivity.class);
        startActivityForResult(intent,form_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (form_Request == requestCode) {
            if (resultCode == RESULT_OK) {
                formsCount++;
                String Name = data.getStringExtra("name");
                String rollNumber = data.getStringExtra("rollNO");
                String semester = data.getStringExtra("semester");
                String cnic = data.getStringExtra("cnic");
                String message = data.getStringExtra("message");
                User applicantId=((MyApplication) getApplication()).getUserActive();
                int i = databaseHelper.checkApplicant(applicantId.getId());
                Applicant applicant;
                boolean result = false;
                if(i==0)
                {
                     applicant = new Applicant(databaseHelper.getApplicantsCount()+1,Name,cnic,applicantId.getId(),rollNumber,semester);
                     result=databaseHelper.addApplicant(applicant);
                    try {
                        ((MyApplication) getApplication()).createApplicant(applicant);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                     applicant = databaseHelper.getApplicant(applicantId.getId());
                    ((MyApplication) getApplication()).checkApplicant(applicant);
                }

                Log.i("Applicant created", "onActivityResult: "+result);
                String approvedBy="";
                int isApproved=0;
                String comments="";
                Form f=new Form(databaseHelper.getFormsCount()+1,applicant.getId(),message,approvedBy,isApproved,comments);
                long res=databaseHelper.addForm(f);
                try {
                    ((MyApplication) getApplication()).createForm(f);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("added form", "onActivityResult: "+res);
                studentsList.add(applicant);
                ListAdapter.notifyDataSetChanged();
            }
        }
    }
}
package com.example.emailactivation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.R;
import com.example.emailactivation.adapters.ApplicantListAdapter;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.models.Form;

import java.util.ArrayList;
import java.util.List;

public class HodDashboardActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ListView lv_Form;
    ApplicantListAdapter ListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_dashboard);
        setTitle("Admin Dashboard");

        lv_Form=findViewById(R.id.lv_formssssss);
        databaseHelper=new DatabaseHelper(this);
        List<Form> formsApprovedByCordinater = databaseHelper.getFormsApprovedByCordinater();


        ((MyApplication) getApplication()).setForms(formsApprovedByCordinater);

        List<Form> forms = ((MyApplication) getApplication()).getForms();
        List<Applicant> applicantList=databaseHelper.getAllApplicants();
        List<Applicant> formApplicants=new ArrayList<>();

        for (Form f: forms) {
            int applicantId=f.getApplicantId();
            for (Applicant applicant:applicantList) {
                if(applicant.getId()==applicantId)
                {
                    formApplicants.add(applicant);
                }
            }
        }
        ((MyApplication) getApplication()).setApplicantsList(formApplicants);

        ListAdapter=new ApplicantListAdapter(formApplicants, HodDashboardActivity.this);
        lv_Form.setAdapter(ListAdapter);

        lv_Form.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HodDashboardActivity.this,HodViewFormActivity.class);
                intent.putExtra("position",  position);
                startActivity(intent);
            }
        });
    }
}
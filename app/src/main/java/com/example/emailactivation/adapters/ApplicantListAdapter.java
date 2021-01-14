package com.example.emailactivation.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emailactivation.R;
import com.example.emailactivation.models.Applicant;

import java.util.ArrayList;
import java.util.List;

public class ApplicantListAdapter extends BaseAdapter {
    List<Applicant> personsList= new ArrayList<>();
    Activity mActivity;

    public ApplicantListAdapter(List<Applicant> personsList, Activity mActivity) {
        this.personsList = personsList;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return personsList.size();
    }

    @Override
    public Object getItem(int position) {
        return personsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View onePersonLine;
        LayoutInflater inflater= (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        onePersonLine=inflater.inflate(R.layout.applicant_one_line,parent,false);
        TextView tv_name=onePersonLine.findViewById(R.id.tv_studentName);
        TextView tv_roll=onePersonLine.findViewById(R.id.tv_studentRoll);
        ImageView iv_icon=onePersonLine.findViewById(R.id.iv_applicantImage);

        Applicant p= (Applicant) this.getItem(position);

        tv_name.setText(p.getName());
        tv_roll.setText(p.getRollNo());
        iv_icon.setImageResource(R.drawable.user);
        return onePersonLine;
    }
}

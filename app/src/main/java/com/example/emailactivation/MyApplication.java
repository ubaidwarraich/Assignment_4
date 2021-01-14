package com.example.emailactivation;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emailactivation.databaseHelper.DatabaseHelper;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.models.Form;
import com.example.emailactivation.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyApplication extends Application{
    public static final String IPADDRESS = "192.168.10.7";
    DatabaseHelper databaseHelper;
    private List<Applicant> applicantsList;
    private User userActive;
    private Applicant applicant;
    private List<Form> forms;

    public List<Applicant> getApplicantsList() {
        return applicantsList;
    }

    public void setApplicantsList(List<Applicant> applicantsList) {
        this.applicantsList = applicantsList;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public List<Form> getForms() {
        return forms;
    }
public Form getForm(int form){
        return forms.get(form);
}
    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public User getUserActive() {
        return userActive;
    }

    public void setUserActive(User userActive) {
        this.userActive = userActive;
    }

    public Form getFormByApplicantId(int id) {
        for (Form f:this.forms) {
            if(f.getApplicantId()==id){
                return f;
            }
        }
        return null;
    }


    public void createAdminAccountsOnServer(String HOD, String CORDINATER) throws JSONException {
        databaseHelper=new DatabaseHelper(this);
        getUserByUsername(HOD,HOD,HOD);
        getUserByUsername(CORDINATER,CORDINATER,CORDINATER);
//        User hod = databaseHelper.Authenticate(HOD);
//        if(hod==null){
//            hod=new User(databaseHelper.getUsersCount()+1,HOD,HOD,HOD);
//            databaseHelper.createUser(hod);
//        }
//        User cordinater = databaseHelper.Authenticate(CORDINATER);
//        if(cordinater==null){
//            cordinater=new User(databaseHelper.getUsersCount()+1,CORDINATER,CORDINATER,CORDINATER);
//            databaseHelper.createUser(cordinater);
//        }
    }

    public void postUser(User user) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", user.getId());
        jo.put("username", user.getUsername());
        jo.put("password",user.getPassword());
        jo.put("role", user.getRole());
        String url= "http://" + IPADDRESS + ":8080/users";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.POST,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("response", "onResponse: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public  void getUserByUsername(String user,String _password,String _role) throws JSONException {
        String username=user;
        String role=_role;
        String password=_password;
        databaseHelper=new DatabaseHelper(this);
        User u = new User();
        String url= "http://" + IPADDRESS + ":8080/users/" +user;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            u.setId(Integer.parseInt(response.getString("id")));
                            u.setUsername(response.getString("username"));
                            u.setPassword(response.getString("password"));
                            u.setRole(response.getString("role"));
                            if(u.getId()==0)
                            {
                             User user=databaseHelper.Authenticate(username);
                             if(user==null){
                                 User userNew = new User(databaseHelper.getUsersCount() + 1, username, _password, _role);
                                 databaseHelper.createUser(userNew);
                                 postUser(userNew);
                             }
                             else{
                                 User userNew = new User(databaseHelper.getUsersCount() + 1, username, _password, _role);
                                 postUser(userNew);
                             }
                            }
                            else {
                                if(_role!="hod" && _role!="cordinater") {
                                    User user = databaseHelper.Authenticate(username);
                                    if (user == null)
                                        databaseHelper.createUser(u);
                                }
                                else{
                                    databaseHelper.createUser(u);
                                }
                            }
                            Log.i("response", "onResponse: "+response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public Applicant getApplicantById(int position){
        return this.applicantsList.get(position);
    }

    public void createApplicant(Applicant applicant) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", applicant.getId());
        jo.put("name", applicant.getName());
        jo.put("rollNo",applicant.getRollNo());
        jo.put("cnic", applicant.getCnic());
        jo.put("semester", applicant.getSemester());
        jo.put("UserId", applicant.getUserId());
        String url= "http://" + IPADDRESS + ":8080/applicants";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.POST,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("response", "onResponse: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void checkApplicant(Applicant applicant) {
        databaseHelper=new DatabaseHelper(this);
        Applicant applicant1 = new Applicant();
        String url= "http://" + IPADDRESS + ":8080/applicants/" +applicant.getId();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            applicant1.setId(Integer.parseInt(response.getString("id")));
                            if(applicant1.getId()==0)
                            {
                                createApplicant(applicant1);
                            }

                            Log.i("response", "onResponse: "+response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void createForm(Form f) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", f.getId());
        jo.put("applicantId", f.getApplicantId());
        jo.put("message",f.getMessage());
        jo.put("approvedBy", f.getApprovedBy());
        jo.put("isApproved", f.isApproved());
        jo.put("comments", f.getComments());
        String url= "http://" + IPADDRESS + ":8080/forms";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.POST,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("response", "onResponse: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void updateForm(Form f) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", f.getId());
        jo.put("applicantId", f.getApplicantId());
        jo.put("message",f.getMessage());
        jo.put("approvedBy", f.getApprovedBy());
        jo.put("isApproved", f.isApproved());
        jo.put("comments", f.getComments());
        String url= "http://" + IPADDRESS + ":8080/forms/" +f.getId();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest (
                Request.Method.PUT,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("response", "onResponse: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error occured", "onResponse: "+error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}

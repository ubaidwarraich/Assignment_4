package com.example.emailactivation.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.emailactivation.MyApplication;
import com.example.emailactivation.models.Applicant;
import com.example.emailactivation.models.Form;
import com.example.emailactivation.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "USERS";
    public static final String ID = "ID";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String SEMESTER = "SEMESTER";
    public static final String CNIC = "CNIC";
    public static final String ROLL_NUMBER = "ROLL_NUMBER";
    public static final String APPLICANT_TABLE = "APPLICANT";
    public static final String APPLICANT_ID = APPLICANT_TABLE + "_ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String ISAPPROVED = "ISAPPROVED";
    public static final String COMMENTS = "COMMENTS";
    public static final String APPROVED_BY = "APPROVED_BY";
    public static final String FORMS_TABLE = "FORMS";
    private static final String ROLE ="ROLE" ;
    private static final String USER_ID ="USER_ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "form.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement;
        createTableStatement= "CREATE TABLE " + USERS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " TEXT," + PASSWORD + " TEXT,"+ROLE+" TEXT)";
        db.execSQL(createTableStatement);
        createTableStatement= "CREATE TABLE " + APPLICANT_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +USER_ID+" TEXT,"+ FULL_NAME + " TEXT," + SEMESTER + " TEXT," + CNIC + " TEXT,"+ROLL_NUMBER+" TEXT)";
        db.execSQL(createTableStatement);
         createTableStatement= "CREATE TABLE " + FORMS_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APPLICANT_ID + " TEXT , " + MESSAGE + " TEXT, " + ISAPPROVED + " INT, " + COMMENTS + " TEXT , " + APPROVED_BY + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+APPLICANT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+FORMS_TABLE);
        onCreate(db);
    }



    public boolean addApplicant(Applicant applicant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues applicantValues=new ContentValues();
        applicantValues.put(ID,applicant.getId());
        applicantValues.put(USER_ID,applicant.getUserId());
        applicantValues.put(FULL_NAME,applicant.getName());
        applicantValues.put(SEMESTER,applicant.getSemester());
        applicantValues.put(CNIC,applicant.getCnic());
        applicantValues.put(ROLL_NUMBER,applicant.getRollNo());
        db.insert(APPLICANT_TABLE, null, applicantValues);
        return true;
    }
    public Applicant getApplicant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int applicantsCount = this.getApplicantsCount();
        String query="Select *from "+APPLICANT_TABLE+" where "+ USER_ID +"="+"'"+id+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Applicant applicant=new Applicant();
            applicant.setId(cursor.getInt(0));
            applicant.setUserId(Integer.parseInt(cursor.getString(1)));
            applicant.setName(cursor.getString(2));
            applicant.setSemester(cursor.getString(3));
            applicant.setCnic(cursor.getString(4));
            applicant.setRollNo(cursor.getString(5));
            return applicant;
        }
        return null;
    }

    public long addForm(Form form){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues formsValues=new ContentValues();
        formsValues.put(ID,form.getId());
        formsValues.put(APPLICANT_ID,form.getApplicantId());
        formsValues.put(MESSAGE,form.getMessage());
        formsValues.put(ISAPPROVED,form.isApproved());
        formsValues.put(COMMENTS,form.getComments());
        formsValues.put(APPROVED_BY,form.getApprovedBy());
         return db.insert(FORMS_TABLE, null, formsValues);
//          return true;
    }

    public boolean createUser(User user){
        SQLiteDatabase databaseHelper=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERNAME,user.getUsername());
        contentValues.put(PASSWORD,user.getPassword());
        contentValues.put(ROLE,user.getRole());
        databaseHelper.insert(USERS_TABLE, null, contentValues);
        return true;
    }
    public User Authenticate(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+USERS_TABLE+" where "+USERNAME+"="+"'"+username+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setRole(cursor.getString(3));
            return user;
        }
        return null;
    }

    public int getUsersCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+USERS_TABLE+" order by "+ID+" desc limit 1";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }



    public int getFormsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+FORMS_TABLE+" order by "+ID+" desc limit 1";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public int getApplicantsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+APPLICANT_TABLE+" order by "+ID+" desc limit 1";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }
    public boolean checkFormApproval(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+APPLICANT_TABLE+" where "+ USER_ID +"="+"'"+userId+"'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int  applicantId=cursor.getInt(0);
            String query2="Select *from "+FORMS_TABLE+" where "+APPLICANT_ID+"="+"'"+applicantId+"'";
            Cursor cursor2= db.rawQuery(query2,null);
            if(cursor2.getCount()>0){
                cursor2.moveToFirst();
                int isApproved= cursor2.getInt(3);
                if(isApproved==1){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return true;
    }

    public int checkApplicant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+APPLICANT_TABLE+" where "+ USER_ID +"="+"'"+id+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public List<Form> getForms(int applicant) {
        List<Form> returnList=new ArrayList<Form>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+FORMS_TABLE+" where "+APPLICANT_ID+"="+"'"+applicant+"'";
        Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()) {
            do{
                  Form form=new Form();
            form.setId(cursor.getInt(0));
            form.setApplicantId(Integer.parseInt(cursor.getString(1)));
            form.setMessage(cursor.getString(2));
            form.setApproved(cursor.getInt(cursor.getColumnIndex("ISAPPROVED")));
            form.setComments(cursor.getString(4));
            form.setApprovedBy(cursor.getString(5));
                returnList.add(form);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
       return returnList;
}

    public List<Form> getAllForms() {
        List<Form> returnList=new ArrayList<Form>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+FORMS_TABLE;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do{
                Form form=new Form();
                form.setId(cursor.getInt(0));
                form.setApplicantId(Integer.parseInt(cursor.getString(1)));
                form.setMessage(cursor.getString(2));
                form.setApproved(cursor.getInt(cursor.getColumnIndex("ISAPPROVED")));
                form.setComments(cursor.getString(4));
                form.setApprovedBy(cursor.getString(5));
                returnList.add(form);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Form> getFormsApprovedByCordinater() {
        List<Form> returnList=new ArrayList<Form>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select *from "+FORMS_TABLE+" where "+ APPROVED_BY +"='pending'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            do{
                Form form=new Form();
                form.setId(cursor.getInt(0));
                form.setApplicantId(Integer.parseInt(cursor.getString(1)));
                form.setMessage(cursor.getString(2));
                form.setApproved(cursor.getInt(cursor.getColumnIndex("ISAPPROVED")));
                form.setComments(cursor.getString(4));
                form.setApprovedBy(cursor.getString(5));
                returnList.add(form);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Applicant> getAllApplicants(){
        List<Applicant> returnList=new ArrayList<Applicant>();

        String queryString="SELECT * FROM "+APPLICANT_TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            do{
                Applicant applicant=new Applicant();
                applicant.setId(cursor.getInt(0));
                applicant.setUserId(Integer.parseInt(cursor.getString(1)));
                applicant.setName(cursor.getString(2));
                applicant.setSemester(cursor.getString(3));
                applicant.setCnic(cursor.getString(4));
                applicant.setRollNo(cursor.getString(5));
                returnList.add(applicant);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean approveForm(int id, String role, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put(ISAPPROVED,1);
        data.put(APPROVED_BY,role);
        data.put(COMMENTS,comments);
        int update = db.update(FORMS_TABLE, data, "ID = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean disapproveForm(int id, String role, String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put(ISAPPROVED,0);
        data.put(APPROVED_BY,role);
        data.put(COMMENTS,s);
        int update = db.update(FORMS_TABLE, data, "ID = ?", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean sendToHod(int id, String role,String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put(ISAPPROVED,0);
        data.put(APPROVED_BY,"pending");
        data.put(COMMENTS,comments);
        int update = db.update(FORMS_TABLE, data, "ID = ?", new String[]{String.valueOf(id)});
        return true;
    }


}

package com.example.finalsports;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tournament_Register extends AppCompatActivity{
    EditText txtTournamentName;
    TextView txtStartDate,txtEndDate;
    String selectedDate="";
    Button btnCreateTournament;
    Tournament tournament;
    Calendar startDate,endDate;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament__register);
        txtTournamentName=(EditText) findViewById(R.id.txtTournamentName);
        txtStartDate=(TextView) findViewById(R.id.txtStartDate);
        txtEndDate=(TextView) findViewById(R.id.txtEndDate);
        startDate= Calendar.getInstance();
        endDate=Calendar.getInstance();
        btnCreateTournament=(Button) findViewById(R.id.btnCreateTournament);
        databaseReference=FirebaseDatabase.getInstance().getReference("Vesp");

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate=null;
                startDate=settingDate(txtStartDate);
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateStr=txtStartDate.getText().toString().trim();
                if(!txtStartDate.getText().toString().trim().equals("")){
                    String arr[]=startDateStr.split("/");
                    int date=Integer.parseInt(arr[0]);
                    int month=Integer.parseInt(arr[1]);
                    int year=Integer.parseInt(arr[2]);
                    startDate.set(year,(month-1),date);
                }
                endDate=settingDate(txtEndDate);
            }
        });
        btnCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create tournament object here!!
                if(checkConditions()){
                    if(College.checkAbsence(txtTournamentName.getText().toString().trim())) {
                        tournament = new Tournament(txtTournamentName.getText().toString().trim(), txtStartDate.getText().toString().trim(), txtEndDate.getText().toString().trim());
                        College.addTournament(tournament);//tournament has been added to the college!
                        databaseReference.child("Tournaments").child(tournament.getTournamentName()).setValue(tournament);
                        Toast.makeText(Tournament_Register.this,tournament.getTournamentName()+" has been added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Tournament_Register.this, New_Sport.class);
                        intent.putExtra("selectedTournament", tournament);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Tournament_Register.this,txtTournamentName.getText().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkConditions(){
        if(!"".equals(txtTournamentName.getText().toString().trim())){
            if(!"".equals(txtStartDate.getText().toString().trim())){
                if(!"".equals(txtEndDate.getText().toString().trim())){
                    if(endDate.after(startDate)) {
                        return true;
                    }else{
                        Toast.makeText(Tournament_Register.this,"End Date must be after Start Date!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(Tournament_Register.this,"Please select an end date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(Tournament_Register.this,"Please select a start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(Tournament_Register.this,"Please enter name of the tournament", Toast.LENGTH_SHORT).show();
        return false;
    }

    private Calendar settingDate(final TextView txtDate){
        AlertDialog.Builder mbldr = new AlertDialog.Builder(Tournament_Register.this);
        View mview = getLayoutInflater().inflate(R.layout.calender_input, null);
        mbldr.setView(mview);
        final AlertDialog aD = mbldr.create();
        aD.show();
        final Calendar calendar=Calendar.getInstance();
        Button btnCancel = (Button) mview.findViewById(R.id.btnCancel);
        Button btnSetDate = (Button) mview.findViewById(R.id.btnSetDate);
        CalendarView calendarView=(CalendarView) mview.findViewById(R.id.calendarView);
        long minDate=calendar.getTimeInMillis();
        if(startDate!=null){
            minDate=startDate.getTimeInMillis();
        }
        calendarView.setMinDate(minDate);
        calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate=dayOfMonth+"/"+(month+1)+"/"+year;
                calendar.set(year,month,dayOfMonth);
            }
        });

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDate.setText(selectedDate);
                aD.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aD.dismiss();
            }
        });
        return calendar;
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,Admin_home.class);
        startActivity(intent);
    }
}
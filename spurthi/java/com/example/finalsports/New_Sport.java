package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class New_Sport extends AppCompatActivity {
    Tournament tournamentMain;
    String[] arrayList;
    ListView popUpList;
    ListView listOfSports;
    DatabaseReference reff;
    EditText txtMaximumNumberOfPlayers;
    TextView txtStartDate,txtEndDate,txtTournamentName;
    RadioButton rbtnBoys,rbtnGirls;
    Button btnAddSport;
    Spinner spnrType,spnrSport;
    String selectedDate="",choice="Individual",gender="Boys";
    Sports sport;
    Calendar startDate,endDate;
    ArrayList<String> arrOfSports;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sport);
        arrOfSports=new ArrayList<>();
        adapter = new ArrayAdapter(New_Sport.this,android.R.layout.simple_list_item_1,arrOfSports);
        Tournament tournament=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournamentMain=College.getTournamentByName(tournament.getTournamentName());
        txtTournamentName=(TextView) findViewById(R.id.txtTournamentName);
        txtStartDate=(TextView) findViewById(R.id.txtStartDate);
        txtEndDate=(TextView) findViewById(R.id.txtEndDate);
        listOfSports=(ListView) findViewById(R.id.listOfSports);
        listOfSports.setAdapter(adapter);
        rbtnBoys=(RadioButton) findViewById(R.id.rbtnBoys);
        rbtnGirls=(RadioButton) findViewById(R.id.rbtnGirls);
        txtMaximumNumberOfPlayers=(EditText) findViewById(R.id.txtMaxNoOfPlayers);
        btnAddSport=(Button) findViewById(R.id.btnAddSport);
        spnrType=(Spinner) findViewById(R.id.spnrType);
        spnrSport=(Spinner) findViewById(R.id.spnrSport);
        startDate=Calendar.getInstance();
        endDate=Calendar.getInstance();
        reff = FirebaseDatabase.getInstance().getReference().child(College.getCollegeName()).child("Tournaments").child(tournamentMain.getTournamentName());
        checkOption(choice);
        txtMaximumNumberOfPlayers.setEnabled(false);
        txtMaximumNumberOfPlayers.setText("1");
        txtTournamentName.setText(tournament.getTournamentName());
        rbtnBoys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Boys";
            }
        });
        rbtnGirls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Girls";
            }
        });
        spnrType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice=spnrType.getSelectedItem().toString().trim();
                checkOption(choice);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btnAddSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sportName = spnrSport.getSelectedItem().toString().trim() + " - " + gender;
                if(checkConditions()){
                    if(tournamentMain.checkAbsence(sportName)) {
                        int count = Integer.parseInt(txtMaximumNumberOfPlayers.getText().toString().trim());
                        String startDate = txtStartDate.getText().toString().trim();
                        String endDate = txtEndDate.getText().toString().trim();
                        if (choice.equals("Team"))
                            sport = new Sports(sportName, choice, count, startDate, endDate);
                        else if (choice.equals("Individual"))
                            sport = new Sports(sportName, choice, 1, startDate, endDate);
                        tournamentMain.addSport(sport);//sport added to tournament!
                        reff.child(sport.getSportName()).setValue(sport);
                        if(Admin_home.operation==Admin_Operations.UPDATE_TOURNAMENT){
                            otherOption();
                            //now go to the activity of update tournament,also if back is pressed then
                        }
                        arrOfSports.add(sport.getSportName());
                        reinitialising();
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(New_Sport.this,sportName+" has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }
                //here you also need to add to the list of sports!

            }
        });
        listOfSports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Sports selectedSport = tournamentMain.getSportByName(arrOfSports.get(position));
                AlertDialog.Builder mbldr = new AlertDialog.Builder(New_Sport.this);
                View mview = getLayoutInflater().inflate(R.layout.pop_up_menu, null);
                mbldr.setView(mview);
                final AlertDialog aD1 = mbldr.create();
                aD1.show();
                popUpList = mview.findViewById(R.id.popUpList);
                popUpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //code to check for edit or delete!
                        aD1.dismiss();
                        String selectedItem = popUpList.getItemAtPosition(position).toString();
                        if (selectedItem.equals("Edit")) {
                            Intent intent1 = new Intent(New_Sport.this, UpdateSport.class);
                            intent1.putExtra("selectedTournament", tournamentMain);
                            intent1.putExtra("selectedSport", selectedSport);
                            startActivity(intent1);
                            //code here to open a new activity..Update Tournament!
                        } else if (selectedItem.equals("Delete")) {
                            AlertDialog.Builder mbldr = new AlertDialog.Builder(New_Sport.this);
                            final View mview = getLayoutInflater().inflate(R.layout.delete_confirmation, null);
                            mbldr.setView(mview);
                            final AlertDialog aD = mbldr.create();
                            aD.show();
                            Button btnYes = mview.findViewById(R.id.btnYes);
                            Button btnNo = mview.findViewById(R.id.btnNo);
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //code here to delete the tournament from the list and database as well!
                                    aD.dismiss();
                                    final DatabaseReference reff1 = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName() + "/" + selectedSport.getSportName());
                                    reff1.removeValue();
                                    tournamentMain.removeSportByName(selectedSport.getSportName());
                                    arrOfSports.remove(selectedSport.getSportName());//removing the element from the list
                                    Toast.makeText(New_Sport.this, selectedSport.getSportName()+ " has been deleted successfully!", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    aD.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

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

    }
    private void checkOption(String choice){
        if(choice.equals("Team")) {
            txtMaximumNumberOfPlayers.setEnabled(true);
            arrayList = getResources().getStringArray(R.array.TeamSports);
        }
        else if(choice.equals("Individual")) {
            txtMaximumNumberOfPlayers.setEnabled(false);
            txtMaximumNumberOfPlayers.setText("1");
            arrayList = getResources().getStringArray(R.array.IndividualSports);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSport.setAdapter(arrayAdapter);
    }
    private boolean checkConditions(){
        if(!"".equals(txtMaximumNumberOfPlayers.getText().toString().trim())){
            if(!"".equals(txtStartDate.getText().toString().trim())){
                if(!"".equals(txtEndDate.getText().toString().trim())){
                    if(endDate.after(startDate)) {
                        return true;
                    }else{
                        Toast.makeText(New_Sport.this,"End Date must be after Start Date!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(New_Sport.this,"Please select an end date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(New_Sport.this,"Please select a start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(New_Sport.this,"Please enter the number of players", Toast.LENGTH_SHORT).show();
        return false;
    }

    private Calendar settingDate(final TextView txtDate){
        AlertDialog.Builder mbldr = new AlertDialog.Builder(New_Sport.this);
        View mview = getLayoutInflater().inflate(R.layout.calender_input, null);
        mbldr.setView(mview);
        final Calendar calendar=Calendar.getInstance();
        final AlertDialog aD = mbldr.create();
        aD.show();
        Button btnCancel = (Button) mview.findViewById(R.id.btnCancel);
        Button btnSetDate = (Button) mview.findViewById(R.id.btnSetDate);
        Calendar minimumDate=getDate(tournamentMain.getStartDate());
        long minDate=minimumDate.getTimeInMillis();
        CalendarView calendarView=(CalendarView) mview.findViewById(R.id.calendarView);
        if(startDate!=null){
            minDate=startDate.getTimeInMillis();
        }
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(getDate(tournamentMain.getEndDate()).getTimeInMillis());
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

    private Calendar getDate(String dateToWorkOn){
        String endDate=dateToWorkOn;
        String arr[]=endDate.split("/");
        int date=Integer.parseInt(arr[0]);
        int month=Integer.parseInt(arr[1]);
        int year=Integer.parseInt(arr[2]);
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,(month-1),date);
        return calendar;
    }

    @Override
    public void onBackPressed(){
        otherOption();
    }
    private void otherOption(){
        Intent i=new Intent(this,UpdateTournament.class);
        i.putExtra("selectedTournament",tournamentMain);
        startActivity(i);
    }
    private void reinitialising(){
        //set the values to its defaults!
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtMaximumNumberOfPlayers.setText("1");
        spnrType.setSelection(0);
        spnrSport.setSelection(0);
        rbtnBoys.setChecked(true);
        gender="Boys";
        startDate=null;
        endDate=null;
        selectedDate="";
        choice="Individual";
    }
}

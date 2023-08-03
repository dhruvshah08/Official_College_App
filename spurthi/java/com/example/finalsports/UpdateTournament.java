package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class UpdateTournament extends AppCompatActivity {
    String selectedDate;

    EditText txtTournamentName;
    TextView txtStartDate,txtEndDate;
    ListView listOfSports;
    ListView popUpList;
    Button btnUpdateTournament;
    ImageButton btnAddSport;
//    Spinner spnrFilter;

    Sports sport;
    Tournament tournamentMain;
    DatabaseReference reff;
    Intent intent1;
    Calendar startDate1,endDate1;
    ArrayList<String> arrOfSports;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tournament);
        arrOfSports=new ArrayList<>();
        txtTournamentName=(EditText) findViewById(R.id.txtTournamentName);
        txtStartDate=(TextView) findViewById(R.id.txtStartDate);
        txtEndDate=(TextView) findViewById(R.id.txtEndDate);
        btnAddSport=(ImageButton) findViewById(R.id.btnAddSport);
        listOfSports=(ListView) findViewById(R.id.listOfSports);
        adapter = new ArrayAdapter(UpdateTournament.this,android.R.layout.simple_list_item_1,arrOfSports);
//        spnrFilter=(Spinner) findViewById(R.id.spnrFilter);
//        filterInit();
        listOfSports.setAdapter(adapter);
        startDate1=Calendar.getInstance();
        endDate1=Calendar.getInstance();
        btnUpdateTournament=(Button) findViewById(R.id.btnUpdateTournament);
        final Tournament tournament=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournamentMain=College.getTournamentByName(tournament.getTournamentName());
        init();
        reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournamentMain.getTournamentName());
        //if(HomeActivity.mode==Login_Mode.ADMIN){
        //now if the tournament with same name is already present then changes wouldn't be saved!
        btnUpdateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournamentMain.getTournamentName());
                String updatedTournamentName=txtTournamentName.getText().toString().trim();
                //if no changes are made in the name..
                if(checkConditions()) {
                    if (!tournamentMain.getTournamentName().equals(updatedTournamentName)) {
                        //check if updated name is not present already!
                        if (College.checkAbsence(updatedTournamentName)) {
                            tournamentMain.setTournamentName(updatedTournamentName);
                            reff.removeValue();
                            reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName());
                            reff.child("Tournaments").child(tournamentMain.getTournamentName()).setValue(tournamentMain);
                            //now adding sports
                            Set<Sports> tempSetOfSports = tournamentMain.getSetOfSports();
                            reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName());
                            for (Sports sport : tempSetOfSports) {
                                //add sports first
                                reff.child(sport.getSportName()).setValue(sport);
                                Set<Team> tempSetOfTeams = sport.getSetOfTeams();
                                reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName() + "/" + sport.getSportName());
                                for (Team team : tempSetOfTeams) {
                                    reff.child(team.getTeamName()).setValue(team);
                                    //now adding the teams to the sport!
                                    //here,players would be added into the team
                                    Set<Player> tempSetOfPlayers = team.getSetOfPlayers();
                                    reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName() + "/" + sport.getSportName() + "/" + team.getTeamName());
                                    for (Player player : tempSetOfPlayers) {
                                        reff.child(player.getName()).setValue(player);
                                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName() + "/" + sport.getSportName() + "/" + team.getTeamName());
                                    }
                                    reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName() + "/" + sport.getSportName());
                                }
                                reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName());
                            }
                            Toast.makeText(UpdateTournament.this,txtTournamentName.getText().toString().trim()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UpdateTournament.this,txtTournamentName.getText().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    String updatedStartDate = txtStartDate.getText().toString().trim();
                    if (!tournamentMain.getStartDate().equals(updatedStartDate)) {
                        tournamentMain.setStartDate(updatedStartDate);
                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName());
                        reff.child("startDate").setValue(updatedStartDate);
                        Toast.makeText(UpdateTournament.this,txtTournamentName.getText().toString().trim()+" has been updated successfully!", Toast.LENGTH_SHORT).show();

                    }
                    String updatedEndDate = txtEndDate.getText().toString().trim();
                    if (!tournamentMain.getEndDate().equals(updatedEndDate)) {
                        tournamentMain.setEndDate(updatedEndDate);
                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournamentMain.getTournamentName());
                        reff.child("endDate").setValue(updatedEndDate);
                        Toast.makeText(UpdateTournament.this,txtTournamentName.getText().toString().trim()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
                //previousActivity();
            }
        });
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Admin_home.operation==Admin_Operations.UPDATE_TOURNAMENT) {
                    startDate1 = null;
                    startDate1 = settingDate(txtStartDate);
                }
            }
        });

        btnAddSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateTournament.this,New_Sport.class);
                intent.putExtra("selectedTournament",tournamentMain);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Admin_home.operation==Admin_Operations.UPDATE_TOURNAMENT) {
                    String startDateStr = txtStartDate.getText().toString().trim();
                    if (!txtStartDate.getText().toString().trim().equals("")) {
                        setter(txtStartDate,startDate1);
                    }
                    endDate1 = settingDate(txtEndDate);
                }
            }
        });
        listOfSports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Sports selectedSport = tournamentMain.getSportByName(arrOfSports.get(position));
                if (HomeActivity.mode == Login_Mode.ADMIN) {
                    AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateTournament.this);
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
                                intent1 = new Intent(UpdateTournament.this, UpdateSport.class);
                                intent1.putExtra("selectedTournament", tournamentMain);
                                intent1.putExtra("selectedSport", selectedSport);
                                startActivity(intent1);
                                //code here to open a new activity..Update Tournament!
                            } else if (selectedItem.equals("Delete")) {
                                AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateTournament.this);
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
                                        tournamentMain.removeSportByName(selectedSport.getSportName());
                                        arrOfSports.remove(selectedSport.getSportName());
                                        reff1.removeValue();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(UpdateTournament.this, selectedSport.getSportName() + " has been deleted successfully!", Toast.LENGTH_SHORT).show();
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
                }else if(HomeActivity.mode==Login_Mode.USER){
                    intent1 = new Intent(UpdateTournament.this, UpdateSport.class);
                    intent1.putExtra("selectedTournament", tournamentMain);
                    intent1.putExtra("selectedSport", selectedSport);
                    startActivity(intent1);
                }
            }
        });
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrOfSports.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                        sport = ds.getValue(Sports.class);
                        arrOfSports.add(sport.getSportName());
                    }
                }
                adapter.notifyDataSetChanged();
                if(arrOfSports.size()==0){
                    Toast.makeText(UpdateTournament.this,"Sorry,no Sports have been added yet!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
//    private void filterInit(){
//        ArrayList<Custom_Choice> arrayList=new ArrayList<>();
//        arrayList.add(new Custom_Choice("Individual Sports",true));
//        arrayList.add(new Custom_Choice("Team Sports",true));
//        Custom_Adapter custom_adapter=new Custom_Adapter(this,arrayList);
//        spnrFilter.setAdapter(custom_adapter);
//
//    }
    private boolean checkConditions(){
        if(!"".equals(txtTournamentName.getText().toString().trim())) {
            if (!"".equals(txtStartDate.getText().toString().trim())) {
                if (!"".equals(txtEndDate.getText().toString().trim())) {
                    if (endDate1.after(startDate1)) {
                        return true;
                    } else {
                        Toast.makeText(UpdateTournament.this, "End Date must be after Start Date!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(UpdateTournament.this, "Please select an end date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(UpdateTournament.this, "Please select a start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(UpdateTournament.this,"Please enter name of the tournament", Toast.LENGTH_SHORT).show();
        return false;
    }
    private void previousActivity(){
        Intent intent=new Intent(this,ViewTournaments.class);
        startActivity(intent);
    }
    private void setter(TextView txtStartDate, Calendar startDate1){
        String startDateStr = txtStartDate.getText().toString().trim();
        String arr1[] = startDateStr.split("/");
        int date = Integer.parseInt(arr1[0]);
        int month = Integer.parseInt(arr1[1]);
        int year = Integer.parseInt(arr1[2]);
        startDate1.set(year, (month - 1), date);
    }
    @Override
    public void onBackPressed(){
        previousActivity();
    }
    private void init(){
        txtTournamentName.setText(tournamentMain.getTournamentName());
        txtStartDate.setText(tournamentMain.getStartDate());
        txtEndDate.setText(tournamentMain.getEndDate());
        setter(txtStartDate,startDate1);
        setter(txtEndDate,endDate1);
        if(HomeActivity.mode==Login_Mode.USER) {
            btnAddSport.setVisibility(View.INVISIBLE);
            details();
        }
        else if(HomeActivity.mode==Login_Mode.ADMIN && Admin_home.operation==Admin_Operations.CREATE_TOURNAMENT){
            details();
        }
    }
    private void details(){
        txtStartDate.setEnabled(false);
        txtEndDate.setEnabled(false);
        txtTournamentName.setEnabled(false);
        btnUpdateTournament.setVisibility(View.INVISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    private Calendar settingDate(final TextView txtDate){
        AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateTournament.this);
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
        //end date
        if(startDate1!=null){
            minDate=startDate1.getTimeInMillis();
            calendarView.setMaxDate(getDate(tournamentMain.getEndDate()).getTimeInMillis());
        }
        //start date
        else{
            //set the minumum date as the current date if startDate par click kiya hai toh!s
            minDate=Calendar.getInstance().getTimeInMillis();
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
}
/*
*In this activity,details of the selected tournament,which'll be existing will be fetched
* Initially all will be set enabled to false
* on edit button click,the components will be set to true!
* on cancel button,back to previous activity,i.e to the view tournaments one!
* on update tournament button click,changed if any will be updated and stored in the db
* note that while no changes are made, update tournament button will be set to false
* on delete,a confirmation will pop-up,if yes then appropriate changes would be made in the db,if cancel,dismiss pop-up
* when tournament will be updated,next activity would be view sports,after selection from that activity,update sport activity would open
* same features on update sport as well!
* next list of teams should appear of the sport and when clicked on one of them,an new activity with its info,editable must open
* same features
* after that,a list of players must be displayed of that sport with the same old characteristics and features.
* view teams.......DONE
* view players.....DONE
* update,
* delete tournament....DONE
* update,
* delete sport....DONE
* update,
* delete team....DONE
*  update,
* delete player....DONE
*/
//BY DEFAULT SET THE MAX DATE TO THE TERM END DATE SOMEWHERE
//everything's done including the update..just see the logic about the reff and the adding and other stuff!
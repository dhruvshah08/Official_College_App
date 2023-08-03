package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class UpdateSport extends AppCompatActivity {
    Tournament tournament;
    Sports sportMain;
    Team team;

    String gender,selectedDate="";

    ListView popUpList;
    ListView listOfTeams;
    Spinner spnrType,spnrSport;
    EditText txtMaxNoOfPlayers;
    TextView txtStartDate,txtEndDate,txtTournamentName;
    RadioButton rbtnBoys,rbtnGirls;
    Button btnUpdateSport;
    ImageButton btnAddTeam;
    String arrayList[];
    ArrayList<String> arrOfTeams;
    Calendar startDate1,endDate1;
    ArrayAdapter<String> adapter;
    DatabaseReference reff;
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sport);
        arrOfTeams=new ArrayList<>();
        spnrType=(Spinner) findViewById(R.id.spnrType);
        spnrSport=(Spinner) findViewById(R.id.spnrSport);
        listOfTeams=(ListView) findViewById(R.id.listOfTeams);
        adapter = new ArrayAdapter(UpdateSport.this,android.R.layout.simple_list_item_1,arrOfTeams);
        listOfTeams.setAdapter(adapter);
        startDate1=Calendar.getInstance();
        endDate1=Calendar.getInstance();
        txtMaxNoOfPlayers=(EditText) findViewById(R.id.txtMaxNoOfPlayers);
        txtStartDate=(TextView) findViewById(R.id.txtStartDate);
        txtEndDate=(TextView) findViewById(R.id.txtEndDate);
        txtTournamentName=(TextView) findViewById(R.id.txtTournamentName);
        rbtnBoys=(RadioButton) findViewById(R.id.rbtnBoys);
        rbtnGirls=(RadioButton) findViewById(R.id.rbtnGirls);
        btnUpdateSport=(Button) findViewById(R.id.btnUpdateSport);
        btnAddTeam=(ImageButton) findViewById(R.id.btnAddTeam);
        Tournament tournament1=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournament=College.getTournamentByName(tournament1.getTournamentName());
        final Sports sport=(Sports) getIntent().getSerializableExtra("selectedSport");
        sportMain=tournament.getSportByName(sport.getSportName());
        init();
        reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sportMain.getSportName());
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrOfTeams.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                        team = ds.getValue(Team.class);
                        arrOfTeams.add(team.getTeamName());
                    }
                }
                adapter.notifyDataSetChanged();
                if(arrOfTeams.size()==0){
                    Toast.makeText(UpdateSport.this,"Sorry,no Teams have been added yet!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        spnrType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Admin_home.operation==Admin_Operations.UPDATE_TOURNAMENT) {
                    String choice = spnrType.getSelectedItem().toString().trim();
                    checkOption(choice);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this should only be the case when either its admin only or when its update only!
                if(Admin_home.operation==Admin_Operations.UPDATE_TOURNAMENT) {
                    startDate1 = null;
                    startDate1 = settingDate(txtStartDate);
                }
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
        listOfTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Team selectedTeam = sportMain.getTeamByName(arrOfTeams.get(position));
                if (HomeActivity.mode == Login_Mode.ADMIN) {
                    AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateSport.this);
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
                                intent1 = new Intent(UpdateSport.this, UpdateTeam.class);
                                intent1.putExtra("selectedTournament", tournament);
                                intent1.putExtra("selectedSport", sportMain);
                                intent1.putExtra("selectedTeam", selectedTeam);
                                startActivity(intent1);
                                //code here to open a new activity..Update Tournament!
                            } else if (selectedItem.equals("Delete")) {
                                AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateSport.this);
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
                                        final DatabaseReference reff1 = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName() + "/" + selectedTeam.getTeamName());
                                        aD.dismiss();
                                        sportMain.removeTeamByName(selectedTeam.getTeamName());
                                        arrOfTeams.remove(selectedTeam.getTeamName());
                                        reff1.removeValue();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(UpdateSport.this, selectedTeam.getTeamName() + " has been deleted successfully!", Toast.LENGTH_SHORT).show();
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
                }else if(HomeActivity.mode==Login_Mode.USER) {
                    Intent intent=new Intent(UpdateSport.this,UpdateTeam.class);
                    intent.putExtra("selectedTournament",tournament);
                    intent.putExtra("selectedSport",sportMain);
                    intent.putExtra("selectedTeam", selectedTeam);
                    startActivity(intent);
                }
            }
        });
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateSport.this,Team_Details.class);
                intent.putExtra("selectedTournament",tournament);
                intent.putExtra("selectedSport",sportMain);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
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
        //now if the sport with same name is already present then changes wouldn't be saved!
        btnUpdateSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to edit the tournament;
                //sport class consists of name(name -  gender) startdate enddate players
                reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sportMain.getSportName());
                String updatedSportName=spnrSport.getSelectedItem().toString().trim() +" - "+gender;
               //if the sportname is not same as previous
                if(checkConditions()) {
                    if (!sportMain.getSportName().equals(updatedSportName)) {
                        //now checking if updated one is is not already added!
                        if (tournament.checkAbsence(updatedSportName)) {
                            sportMain.setSportName(updatedSportName);
                            String updatedSportType = spnrType.getSelectedItem().toString().trim();
                            sportMain.setSportType(updatedSportType);
                            reff.removeValue();
                            reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName());
                            reff.child(sportMain.getSportName()).setValue(sportMain);
                            //now here first get teams and then in each team players should be added as well!
                            Set<Team> tempSetOfTeams = sportMain.getSetOfTeams();
                            reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName());
                            for (Team team : tempSetOfTeams) {
                                reff.child(team.getTeamName()).setValue(team);
                                //now adding the teams to the sport!
                                //here,players would be added into the team
                                Set<Player> tempSetOfPlayers = team.getSetOfPlayers();
                                reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName() + "/" + team.getTeamName());
                                for (Player player : tempSetOfPlayers) {
                                    reff.child(player.getName()).setValue(player);
                                    reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName() + "/" + team.getTeamName());
                                }
                                reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName());
                            }
                            Toast.makeText(UpdateSport.this,sportMain.getSportName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(UpdateSport.this,sportMain.getSportName()+" has already been added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    String updatedStartDate = txtStartDate.getText().toString().trim();
                    if (!sportMain.getStartDate().equals(updatedStartDate)) {
                        sportMain.setStartDate(updatedStartDate);
                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName());
                        reff.child("startDate").setValue(updatedStartDate);
                        Toast.makeText(UpdateSport.this,sportMain.getSportName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                    String updatedEndDate = txtEndDate.getText().toString().trim();
                    if (!sportMain.getEndDate().equals(updatedEndDate)) {
                        sportMain.setEndDate(updatedEndDate);
                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName());
                        reff.child("endDate").setValue(updatedEndDate);
                        Toast.makeText(UpdateSport.this,sportMain.getSportName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                    int updatedNo = Integer.parseInt(txtMaxNoOfPlayers.getText().toString().trim());
                    if (sportMain.getMaxNoOfPlayers() != (updatedNo)) {
                        sportMain.setMaxNoOfPlayers(updatedNo);
                        reff = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sportMain.getSportName());
                        reff.child("maxNoOfPlayers").setValue(updatedNo);
                        Toast.makeText(UpdateSport.this,sportMain.getSportName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
                //create a new sportName variable which would be used
            }
        });

    }
    private void checkOption(String choice){
        if(choice.equals("Team")) {
            txtMaxNoOfPlayers.setEnabled(true);
            arrayList = getResources().getStringArray(R.array.TeamSports);
        }
        else if(choice.equals("Individual")) {
            txtMaxNoOfPlayers.setEnabled(false);
            txtMaxNoOfPlayers.setText("1");
            arrayList = getResources().getStringArray(R.array.IndividualSports);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSport.setAdapter(arrayAdapter);
    }
    private void previousActivity(){
        Intent intent=new Intent(this,UpdateTournament.class);
        intent.putExtra("selectedTournament",tournament);
        startActivity(intent);
    }
    private boolean checkConditions(){
        if(!"".equals(txtMaxNoOfPlayers.getText().toString().trim())){
            if(!"".equals(txtStartDate.getText().toString().trim())){
                if(!"".equals(txtEndDate.getText().toString().trim())){
                    if(endDate1.after(startDate1)) {
                        return true;
                    }else{
                        Toast.makeText(UpdateSport.this,"End Date must be after Start Date!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(UpdateSport.this,"Please select an end date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(UpdateSport.this,"Please select a start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(UpdateSport.this,"Please enter the number of players", Toast.LENGTH_SHORT).show();
        return false;
    }
    @Override
    public void onBackPressed() {
        previousActivity();
    }
    private void init(){
        txtTournamentName.setText(tournament.getTournamentName());
        txtMaxNoOfPlayers.setText(sportMain.getMaxNoOfPlayers()+"");
        txtStartDate.setText(sportMain.getStartDate());
        txtEndDate.setText(sportMain.getEndDate());
        if(sportMain.getSportName().indexOf("Boys")>=0){
            rbtnBoys.setChecked(true);
            gender="Boys";
        }else if(sportMain.getSportName().indexOf("Girls")>=0){
            rbtnGirls.setChecked(true);
            gender="Girls";
        }
        String arr[];
        if(sportMain.getSportType().equals("Team")) {
            arr = getResources().getStringArray(R.array.TeamSports);
            spnrType.setSelection(1);
        }
        else{
            arr = getResources().getStringArray(R.array.IndividualSports);
            spnrType.setSelection(0);
            txtMaxNoOfPlayers.setEnabled(false);
            txtMaxNoOfPlayers.setText("1");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSport.setAdapter(arrayAdapter);
        int pos=findIndex(arr,sportMain.getSportName());
        spnrSport.setSelection(pos);
        //setting values

        if(HomeActivity.mode==Login_Mode.USER){
            btnAddTeam.setVisibility(View.INVISIBLE);
            details();
        }
        else if(HomeActivity.mode==Login_Mode.ADMIN && Admin_home.operation==Admin_Operations.CREATE_TOURNAMENT){
            //btnAddTeam.setVisibility(View.VISIBLE);
            details();
        }
        setter(txtStartDate,startDate1);
        setter(txtEndDate,endDate1);
    }
    private void setter(TextView txtStartDate, Calendar startDate1){
        String startDateStr = txtStartDate.getText().toString().trim();
        String arr1[] = startDateStr.split("/");
        int date = Integer.parseInt(arr1[0]);
        int month = Integer.parseInt(arr1[1]);
        int year = Integer.parseInt(arr1[2]);
        startDate1.set(year, (month - 1), date);
    }
    private void details(){
        disableAll();
        btnUpdateSport.setVisibility(View.INVISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void disableAll(){
        spnrType.setEnabled(false);
        spnrSport.setEnabled(false);
        txtEndDate.setEnabled(false);
        txtStartDate.setEnabled(false);
        txtMaxNoOfPlayers.setEnabled(false);
        rbtnBoys.setEnabled(false);
        rbtnGirls.setEnabled(false);
    }

    private int findIndex(String arr[],String sel){
        for(int i=0;i<arr.length;i++){
            if((sel.indexOf(arr[i]))>=0){
                return i;
            }
        }
        return -1;
    }
    private Calendar settingDate(final TextView txtDate){
        AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateSport.this);
        View mview = getLayoutInflater().inflate(R.layout.calender_input, null);
        mbldr.setView(mview);
        final Calendar calendar=Calendar.getInstance();
        final AlertDialog aD = mbldr.create();
        aD.show();
        Button btnCancel = (Button) mview.findViewById(R.id.btnCancel);
        Button btnSetDate = (Button) mview.findViewById(R.id.btnSetDate);
        Calendar minimumDate=getDate(tournament.getStartDate());
        long minDate=minimumDate.getTimeInMillis();
        CalendarView calendarView=(CalendarView) mview.findViewById(R.id.calendarView);
        if(startDate1!=null){
            minDate=startDate1.getTimeInMillis();
        }
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(getDate(tournament.getEndDate()).getTimeInMillis());
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
//next thing to do..
//add into the list after adding in the update
//change in the User login->only view everything!
package com.example.finalsports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Team_Details extends AppCompatActivity {
    Spinner txtTeamName;
    Button btnAddTeam;
    TextView txtSportName;
    Team team;
    Sports sportMain;
    DatabaseReference reff;
    Tournament tournament;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team__details);
        txtSportName=(TextView) findViewById(R.id.txtSportName);
        Tournament tournament1=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournament=College.getTournamentByName(tournament1.getTournamentName());
        Sports sport=(Sports)getIntent().getSerializableExtra("selectedSport");
        sportMain=tournament.getSportByName(sport.getSportName());
        txtTeamName=(Spinner) findViewById(R.id.txtTeamName);
        btnAddTeam=(Button) findViewById(R.id.btnAddTeam);
        txtSportName.setText(sportMain.getSportName());
        reff= FirebaseDatabase.getInstance().getReference().child(College.getCollegeName().trim()).child("Tournaments").child(tournament.getTournamentName()).child(sportMain.getSportName());
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to create a new team object with name as text of team name goes here!!
                if(sportMain.checkAbsence(txtTeamName.getSelectedItem().toString().trim())) {
                    team = new Team(txtTeamName.getSelectedItem().toString().trim());
                    sportMain.addTeam(team);
                    reff.child(team.getTeamName()).setValue(team);
                    Toast.makeText(Team_Details.this,team.getTeamName()+" has been added successfully!", Toast.LENGTH_SHORT).show();
                    nextStep();
                }else{
                    Toast.makeText(Team_Details.this,txtTeamName.getSelectedItem().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        nextStep();
    }
    private void nextStep(){
        Intent intent=new Intent(this,UpdateSport.class);
        intent.putExtra("selectedTournament",tournament);
        intent.putExtra("selectedSport",sportMain);
        startActivity(intent);
    }

}

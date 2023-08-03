package com.example.finalsports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPlayer extends AppCompatActivity {
    Tournament tournament;
    Sports sport;
    Team teamMain;
    Button btnAddPlayer;
    Player player;
    EditText txtName,txtYear;
    TextView txtEventName,txtTeamName;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_player);
        btnAddPlayer= (Button) findViewById(R.id.btnAddPlayer);
        txtName= (EditText) findViewById(R.id.txtPlayerName);
        txtYear= (EditText) findViewById(R.id.txtPlayerYear);
        txtEventName=(TextView)findViewById(R.id.txtEventName);
        txtTeamName=(TextView) findViewById(R.id.txtTeamName);
        Tournament tournament1=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournament=College.getTournamentByName(tournament1.getTournamentName());
        Sports sport1=(Sports) getIntent().getSerializableExtra("selectedSport");
        sport=tournament.getSportByName(sport1.getSportName());
        Team team=(Team)getIntent().getSerializableExtra("selectedTeam");
        teamMain=sport.getTeamByName(team.getTeamName());
        txtEventName.setText(sport.getSportName());
        txtTeamName.setText(teamMain.getTeamName());
        reff= FirebaseDatabase.getInstance().getReference().child(College.getCollegeName().trim()).child("Tournaments").child(tournament.getTournamentName()).child(sport.getSportName()).child(teamMain.getTeamName());
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teamMain.checkAbsence(txtName.getText().toString().trim())){
                    String name = txtName.getText().toString().trim();
                    int year = Integer.parseInt(txtYear.getText().toString().trim());
                    if(year>=1 && year<=3) {
                        player = new Player(name, year);
                        teamMain.addPlayer(player);
                        reff.child(player.getName()).setValue(player);
                        Toast.makeText(NewPlayer.this,player.getName()+" has been added successfully!", Toast.LENGTH_SHORT).show();
                        nextStep();
                    }else{
                        Toast.makeText(NewPlayer.this,"Invalid Year!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(NewPlayer.this,txtName.getText().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                }
                //add player to the team! and to the list as well!
            }
        });
    }

    @Override
    public void onBackPressed(){
        nextStep();
    }
    private void nextStep(){
        Intent intent=new Intent(this,UpdateTeam.class);
        intent.putExtra("selectedTournament",tournament);
        intent.putExtra("selectedSport",sport);
        intent.putExtra("selectedTeam",teamMain);
        startActivity(intent);
    }
}
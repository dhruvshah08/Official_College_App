package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePlayer extends AppCompatActivity {
    Tournament tournament;
    Sports sport;
    Team team;
    Player playerMain;
    DatabaseReference reff;
    Button btnUpdatePlayer,btnHome;
    EditText txtPlayerName,txtPlayerYear;
    TextView txtEventName,txtTeamName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_player);
        txtEventName=(TextView) findViewById(R.id.txtEventName);
        txtTeamName=(TextView) findViewById(R.id.txtTeamName);
        txtPlayerYear=(EditText) findViewById(R.id.txtPlayerYear);
        txtPlayerName=(EditText) findViewById(R.id.txtPlayerName);
        btnUpdatePlayer=(Button) findViewById(R.id.btnUpdatePlayer);
        btnHome=(Button) findViewById(R.id.btnHomeButton);
        Tournament tournament1=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournament=College.getTournamentByName(tournament1.getTournamentName());
        Sports sport1=(Sports) getIntent().getSerializableExtra("selectedSport");
        sport=tournament.getSportByName(sport1.getSportName());
        Team team1=(Team) getIntent().getSerializableExtra("selectedTeam");
        team=sport.getTeamByName(team1.getTeamName());
        Player player=(Player) getIntent().getSerializableExtra("selectedPlayer");
        playerMain=team.getPlayerByName(player.getName());
        init();
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UpdatePlayer.this,ViewTournaments.class);
                startActivity(i);
            }
        });
        btnUpdatePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if either of them is different then only modifications to be made
                reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+team.getTeamName()+"/"+playerMain.getName());
                String updatedPlayerName=txtPlayerName.getText().toString().trim();
                int year=Integer.parseInt(txtPlayerYear.getText().toString().trim());
                if(year>=1 && year<=3){
                    if(!playerMain.getName().equalsIgnoreCase(updatedPlayerName)){
                        if(team.checkAbsence(updatedPlayerName)) {
                            playerMain.setName(updatedPlayerName);
                            //now here first remove this reference first
                            reff.removeValue();
                            //and then go back upto team and then again add pla
                            reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+team.getTeamName());
                            reff.child(playerMain.getName()).setValue(playerMain);
                            Toast.makeText(UpdatePlayer.this,playerMain.getName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(UpdatePlayer.this,txtPlayerName.getText().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    int updatedPlayerYear=Integer.parseInt(txtPlayerYear.getText().toString().trim());
                    if(playerMain.getYear()!=updatedPlayerYear){
                        playerMain.setYear(updatedPlayerYear);
                        reff=FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+team.getTeamName()+"/"+playerMain.getName());
                        reff.child("year").setValue(updatedPlayerYear);
                        Toast.makeText(UpdatePlayer.this,playerMain.getName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                    team.printAllPlayers();
                }else{
                    Toast.makeText(UpdatePlayer.this,"Invalid Year!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void previousActivity() {
        Intent intent = new Intent(UpdatePlayer.this, UpdateTeam.class);
        intent.putExtra("selectedTournament", tournament);
        intent.putExtra("selectedSport", sport);
        intent.putExtra("selectedTeam", team);
        startActivity(intent);
    }

    private void init(){
        txtEventName.setText(sport.getSportName());
        txtTeamName.setText(team.getTeamName());
        txtPlayerName.setText(playerMain.getName());
        txtPlayerYear.setText(playerMain.getYear()+"");

        if(HomeActivity.mode==Login_Mode.USER || (HomeActivity.mode==Login_Mode.ADMIN && Admin_home.operation==Admin_Operations.CREATE_TOURNAMENT)){
            txtPlayerYear.setEnabled(false);
            txtPlayerName.setEnabled(false);
            btnUpdatePlayer.setVisibility(View.INVISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
    @Override
    public void onBackPressed(){
        previousActivity();
    }
}
/*
*So here,if user has logged in,then update button mustn't be seen!
* If the admin has logged in,and create is type then also,update option must not be available
also the two text fields must be not editable!
 */
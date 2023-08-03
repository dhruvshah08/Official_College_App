package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class UpdateTeam extends AppCompatActivity {
    Tournament tournament;
    Sports sport;
    Team teamMain;
    Player player;

    TextView txtSportName;
    Spinner txtTeamName;
    Button btnUpdateTeam;
    ImageButton btnAddPlayer;
    ListView listOfPlayers,popUpList;

    ArrayList<String> arrOfPlayers;
    ArrayAdapter<String> adapter;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_team);
        arrOfPlayers=new ArrayList<>();
        txtSportName=(TextView) findViewById(R.id.txtSportName);
        txtTeamName=(Spinner) findViewById(R.id.txtTeamName);
        btnUpdateTeam=(Button) findViewById(R.id.btnUpdateTeam);
        btnAddPlayer=(ImageButton) findViewById(R.id.btnAddPlayer);
        listOfPlayers=(ListView) findViewById(R.id.listOfPlayers);
        adapter = new ArrayAdapter(UpdateTeam.this,android.R.layout.simple_list_item_1,arrOfPlayers);
        listOfPlayers.setAdapter(adapter);
        Tournament tournament1=(Tournament) getIntent().getSerializableExtra("selectedTournament");
        tournament=College.getTournamentByName(tournament1.getTournamentName());
        Sports sport1=(Sports) getIntent().getSerializableExtra("selectedSport");
        sport=tournament.getSportByName(sport1.getSportName());
        Team team=(Team) getIntent().getSerializableExtra("selectedTeam");
        teamMain=sport.getTeamByName(team.getTeamName());
        init();
        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+teamMain.getTeamName());
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrOfPlayers.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                        player = ds.getValue(Player.class);
                        arrOfPlayers.add(player.getName());
                    }
                }
                adapter.notifyDataSetChanged();
                if(arrOfPlayers.size()==0){
                    Toast.makeText(UpdateTeam.this,"Sorry,no Players have been added yet!", Toast.LENGTH_LONG).show();
                }
                if(arrOfPlayers.size()==sport.getMaxNoOfPlayers()){
                    btnAddPlayer.setEnabled(false);
                    Toast.makeText(UpdateTeam.this,"All slots have been filled!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listOfPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Player selectedPlayer = teamMain.getPlayerByName(arrOfPlayers.get(position));
                if(HomeActivity.mode==Login_Mode.ADMIN) {
                    AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateTeam.this);
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
                                Intent intent1 = new Intent(UpdateTeam.this, UpdatePlayer.class);
                                intent1.putExtra("selectedTournament", tournament);
                                intent1.putExtra("selectedSport", sport);
                                intent1.putExtra("selectedTeam", teamMain);
                                intent1.putExtra("selectedPlayer", selectedPlayer);
                                startActivity(intent1);
                                //code here to open a new activity..Update Tournament!
                            } else if (selectedItem.equals("Delete")) {
                                AlertDialog.Builder mbldr = new AlertDialog.Builder(UpdateTeam.this);
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
                                        final DatabaseReference reff1 = FirebaseDatabase.getInstance().getReference(College.getCollegeName() + "/Tournaments/" + tournament.getTournamentName() + "/" + sport.getSportName() + "/" + teamMain.getTeamName() + "/" + selectedPlayer.getName());
                                        aD.dismiss();
                                        teamMain.removePlayerByName(selectedPlayer.getName());
                                        arrOfPlayers.remove(selectedPlayer.getName());
                                        reff1.removeValue();
                                        adapter.notifyDataSetChanged();
                                        if (!btnAddPlayer.isEnabled()) {
                                            btnAddPlayer.setEnabled(true);
                                        }
                                        Toast.makeText(UpdateTeam.this, selectedPlayer.getName() + " has been deleted successfully!", Toast.LENGTH_SHORT).show();
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
                    Intent intent1 = new Intent(UpdateTeam.this, UpdatePlayer.class);
                    intent1.putExtra("selectedTournament", tournament);
                    intent1.putExtra("selectedSport", sport);
                    intent1.putExtra("selectedTeam", teamMain);
                    intent1.putExtra("selectedPlayer", selectedPlayer);
                    startActivity(intent1);
                }
            }
        });

        btnUpdateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+teamMain.getTeamName());
                String updatedTeamName=txtTeamName.getSelectedItem().toString().trim();
                if(!teamMain.getTeamName().equalsIgnoreCase(updatedTeamName)){
                    if(sport.checkAbsence(updatedTeamName)) {
                        teamMain.setTeamName(updatedTeamName);
                        reff.removeValue();
                        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName());
                        reff.child(teamMain.getTeamName()).setValue(teamMain);
                        Set<Player> tempSetOfPlayers=teamMain.getSetOfPlayers();
                        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+teamMain.getTeamName());
                        for(Player player:tempSetOfPlayers){
                            reff.child(player.getName()).setValue(player);
                            reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+teamMain.getTeamName());
                        }
                        //here if there are players then that too must be adjusted!
                    }
                    Toast.makeText(UpdateTeam.this,teamMain.getTeamName()+" has been updated successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UpdateTeam.this,txtTeamName.getSelectedItem().toString().trim()+" has already been added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(UpdateTeam.this,NewPlayer.class);
                intent1.putExtra("selectedTournament",tournament);
                intent1.putExtra("selectedSport",sport);
                intent1.putExtra("selectedTeam",teamMain);
                startActivity(intent1);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void previousActivity(){
        Intent intent=new Intent(this,UpdateSport.class);
        intent.putExtra("selectedTournament",tournament);
        intent.putExtra("selectedSport",sport);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        previousActivity();
    }

    private void init(){
        txtSportName.setText(sport.getSportName());
        String arr[]=getResources().getStringArray(R.array.branches);
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(teamMain.getTeamName())){
                txtTeamName.setSelection(i);
                break;
            }
        }

        if(HomeActivity.mode==Login_Mode.USER){
            btnAddPlayer.setVisibility(View.INVISIBLE);
            details();
        }
        else if(HomeActivity.mode==Login_Mode.ADMIN && Admin_home.operation==Admin_Operations.CREATE_TOURNAMENT){
            details();
        }
    }
    private void details(){
        txtTeamName.setEnabled(false);
        btnUpdateTeam.setVisibility(View.INVISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
//now first check if the key,i.e name is different...if that is same then okay continue if it's not then check if new is already present
//if yes then name can't be changed and the rest operations would happen i.e editing of other fields!
//updations in list must also be done,no elements are been shown!
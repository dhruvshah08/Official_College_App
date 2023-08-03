package com.example.finalsports;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrievePlayers extends RetrieveTeam implements Runnable {
    private Player player;
    private Team team;
    private Tournament tournament;
    private Sports sport;
    private DatabaseReference reff;
    RetrievePlayers(Tournament tournament,Sports sport,Team team){
        super(tournament,sport);
        this.tournament=tournament;
        this.sport=sport;
        this.team=team;
        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sport.getSportName()+"/"+team.getTeamName());
    }
    @Override
    public void run(){
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                    player = new Player();
                    player = ds.getValue(Player.class);
                    team.addPlayer(player);
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }
}

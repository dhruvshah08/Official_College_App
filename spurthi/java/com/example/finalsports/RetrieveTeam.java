package com.example.finalsports;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrieveTeam extends RetrieveSports implements Runnable {
    private Sports sports;
    private DatabaseReference reffTeam;
    private Thread thread;
    private Team team;
    private Tournament tournament;
    RetrieveTeam(Tournament tournament,Sports sports){
        super(tournament);
        this.tournament=tournament;
        this.sports=sports;
        reffTeam= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName()+"/"+sports.getSportName());
    }
    @Override
    public void run(){
        reffTeam.addListenerForSingleValueEvent (new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                    team = new Team();
                    team = ds.getValue(Team.class);
                    sports.addTeam(team);
                    thread = new Thread(new RetrievePlayers(tournament, sports, team));
                    thread.start();
                    while (thread.isAlive()) {
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }
}

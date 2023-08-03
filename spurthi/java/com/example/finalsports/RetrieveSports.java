package com.example.finalsports;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrieveSports extends RetrieveTournaments implements Runnable{
    private Sports sport;
    private Thread thread;
    private DatabaseReference reffSport;
    private Tournament tournament;
    RetrieveSports(Tournament tournament){
        this.tournament=tournament;
        reffSport= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments/"+tournament.getTournamentName());
    }
    @Override
    public void run(){
        reffSport.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                    sport = new Sports();
                    sport = ds.getValue(Sports.class);
                    tournament.addSport(sport);
                    thread=new Thread(new RetrieveTeam(tournament, sport));
                    thread.start();
                    while(thread.isAlive()){}
                    //here..new Thread which will read team name
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }
}

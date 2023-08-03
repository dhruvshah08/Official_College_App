package com.example.finalsports;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrieveTournaments implements Runnable {
    private Tournament tournament;
    private Thread thread;
    private DatabaseReference reffTournament;
    RetrieveTournaments(){
        reffTournament= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Tournaments");
    }
    @Override
    public void run(){
        reffTournament.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                    tournament=new Tournament();
                    tournament=ds.getValue(Tournament.class);
                    College.addTournament(tournament);
                    //begin a new thread from here,passing name and get info of data under it
                    thread=new Thread(new RetrieveSports(tournament));
                    thread.start();
                    while(thread.isAlive()){}
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
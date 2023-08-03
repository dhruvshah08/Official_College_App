package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTournaments extends AppCompatActivity {
    ListView listOfTournaments;
    ListView popUpList;
    TextView txtCollegeName;
    DatabaseReference reff;
    Intent intent1;
    ArrayList<String> arrOfTournaments;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tournaments);
        arrOfTournaments=new ArrayList<>();
        listOfTournaments=(ListView)findViewById(R.id.listOfTournaments);
        adapter = new ArrayAdapter(ViewTournaments.this,android.R.layout.simple_list_item_1,arrOfTournaments);
        listOfTournaments.setAdapter(adapter);
        txtCollegeName=(TextView) findViewById(R.id.txtCollegeName);
        txtCollegeName.setText(College.getCollegeName());
        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName().trim()+"/Tournaments");
        Admin_home.operation=Admin_Operations.UPDATE_TOURNAMENT;
        listOfTournaments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //code here to open a new activity and pass the name of the selected item!
                final Tournament selectedTournament = College.getTournamentByName(arrOfTournaments.get(position));
                if (HomeActivity.mode == Login_Mode.ADMIN) {
                    //a new dialog would open here for option of edit or delete!
                    AlertDialog.Builder mbldr = new AlertDialog.Builder(ViewTournaments.this);
                    View mview = getLayoutInflater().inflate(R.layout.pop_up_menu, null);
                    mbldr.setView(mview);
                    final AlertDialog aD1 = mbldr.create();
                    aD1.show();
                    popUpList = mview.findViewById(R.id.popUpList);
                    popUpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            //code to check for edit or delete!
                            aD1.dismiss();
                            String selectedItem = popUpList.getItemAtPosition(position).toString();//edit or delete
                            if (selectedItem.equals("Edit")) {
                                intent1 = new Intent(ViewTournaments.this, UpdateTournament.class);
                                intent1.putExtra("selectedTournament", selectedTournament);
                                startActivity(intent1);
                                //code here to open a new activity..Update Tournament!
                            } else if (selectedItem.equals("Delete")) {
                                //code to delete the tournament!
                                AlertDialog.Builder mbldr = new AlertDialog.Builder(ViewTournaments.this);
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
                                        final DatabaseReference reff1 = FirebaseDatabase.getInstance().getReference(College.getCollegeName().trim() + "/Tournaments/" + selectedTournament.getTournamentName());
                                        aD.dismiss();
                                        College.removeTournamentByName(selectedTournament.getTournamentName());
                                        arrOfTournaments.remove(selectedTournament.getTournamentName());
                                        reff1.removeValue();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ViewTournaments.this, selectedTournament.getTournamentName()+ " has been deleted successfully!", Toast.LENGTH_SHORT).show();
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
                } else if (HomeActivity.mode == Login_Mode.USER) {
                    intent1 = new Intent(ViewTournaments.this, UpdateTournament.class);
                    intent1.putExtra("selectedTournament", selectedTournament);
                    startActivity(intent1);
                }
            }
        });
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrOfTournaments.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if (ds.getValue().getClass().equals(java.util.HashMap.class)) {
                        Tournament tournament = ds.getValue(Tournament.class);
                        arrOfTournaments.add(tournament.getTournamentName());
                    }
                }
                adapter.notifyDataSetChanged();
                if(arrOfTournaments.size()==0){
                    Toast.makeText(ViewTournaments.this,"Sorry,no Tournaments have been added yet!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=null;
        if(HomeActivity.mode==Login_Mode.USER){

        }else if(HomeActivity.mode==Login_Mode.ADMIN){
            intent=new Intent(ViewTournaments.this,Admin_home.class);
            startActivity(intent);
        }
    }

}

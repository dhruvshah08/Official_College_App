package com.example.audi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audi.slot1_booking;
import com.example.audi.slot2_booking;
import com.example.audi.slot3_booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class avail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    CalendarView calendarView;
    Button slot1,slot2,slot3;
    Button bookBtn;
    TextView selectDateTV;
    String slot1_selected,slot2_selected,slot3_selected;
    int yy,mm,dd;
    String current_date;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference date,currentRef,myRef,myRef2,myRef3;
    String slots="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String pattern = "dd-MM-yyyy";
        String dateInString =new SimpleDateFormat(pattern).format(calendarView.getDate());
        Date datex=new Date(calendarView.getDate());
        current_date=""+datex.getDate()+"-"+(datex.getMonth()+1)+"-"+Calendar.getInstance().get(Calendar.YEAR);

        AlertDialog.Builder alert = new AlertDialog.Builder(avail.this);
        final View mView = getLayoutInflater().inflate(R.layout.alert_lay,null);
        final Button ok_btn = (Button) mView.findViewById(R.id.ok_btn);
        final TextView confirm = (TextView) mView.findViewById(R.id.confirm_tv);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dd=i2;
                mm=i1+1;
                yy=i;
                current_date=""+dd+"-"+mm+"-"+yy;

                currentRef=firebaseDatabase.getReference().child(current_date);


                currentRef.addValueEventListener(new ValueEventListener() {
                    slot1_booking slot1_obj;
                    slot2_booking slot2_obj;
                    slot3_booking slot3_obj;
                    int counter=1;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        date=firebaseDatabase.getReference();
                        myRef=date.child(current_date).child("slot1");

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(avail.this, "Here for 1", Toast.LENGTH_SHORT).show();
                                    slot1.setBackgroundColor(Color.RED);
                                    slot1.setEnabled(false);
                                }
                                else{
                                    Toast.makeText(avail.this, "not Here for 1", Toast.LENGTH_SHORT).show();
                                    slot1.setBackgroundColor(Color.WHITE);
                                    slot1.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRef2=date.child(current_date).child("slot2");

                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(avail.this, "Here for 2", Toast.LENGTH_SHORT).show();
                                    slot2.setBackgroundColor(Color.RED);
                                    slot2.setEnabled(false);
                                }
                                else{
                                    Toast.makeText(avail.this, "not Here for 2", Toast.LENGTH_SHORT).show();
                                    slot2.setBackgroundColor(Color.WHITE);
                                    slot2.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        myRef3=date.child(current_date).child("slot3");

                        myRef3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(avail.this, "Here for 3", Toast.LENGTH_SHORT).show();
                                    slot3.setBackgroundColor(Color.RED);
                                    slot3.setEnabled(false);
                                }
                                else{
                                    Toast.makeText(avail.this, "not Here for 3", Toast.LENGTH_SHORT).show();
                                    slot3.setBackgroundColor(Color.WHITE);
                                    slot3.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


//                        if(dataSnapshot.exists()){
//
//                            for (DataSnapshot ds : dataSnapshot.getChildren()){
//                                if(counter==1) {
//                                    slot1_obj = ds.getValue(slot1_booking.class);
//                                }
//                                if(counter==2){
//                                    slot2_obj=ds.getValue(slot2_booking.class);
//
//                                }
//                                if(counter==3){
//                                    slot3_obj=ds.getValue(slot3_booking.class);
//
//                                }
//                                counter++;
//
//                                try {
//                                    if(slot1_obj.getStatus().equals("true")){
//                                        slot1.setBackgroundColor(Color.RED);
//                                        slot1.setEnabled(false);
//                                        slot1_selected="true";
////                                        Toast.makeText(avail.this, "slot 1 not avail", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        slot1.setBackgroundColor(Color.WHITE);
////                                        Toast.makeText(avail.this, "1 avail", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                    if(slot2_obj.getStatus().equals("true")){
//                                        slot2.setBackgroundColor(Color.RED);
//                                        slot2_selected="true";
//                                        slot2.setEnabled(false);
////                                        Toast.makeText(avail.this, "slot 2 not avail", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
////                                        Toast.makeText(avail.this, "2 avail", Toast.LENGTH_SHORT).show();
//                                        slot2.setBackgroundColor(Color.WHITE);
//                                    }
//                                    if(slot3_obj.getStatus().equals("true")){
//                                        slot3_selected="true";
//                                        slot3.setBackgroundColor(Color.RED);
//                                        slot3.setEnabled(false);
////                                        Toast.makeText(avail.this, "slot 3 not avail", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
////                                        Toast.makeText(avail.this, "slot 3 avail", Toast.LENGTH_SHORT).show();
//                                        slot3.setBackgroundColor(Color.WHITE);
//                                    }
//
//                                }
//                                catch (Exception e){
//
//                                }
//                            }
//
//                        }
//                        else{
//
//                            slot1.setEnabled(true);
//                            slot2.setEnabled(true);
//                            slot3.setEnabled(true);
//
//                            slot1.setBackgroundColor(Color.WHITE);
//                            slot2.setBackgroundColor(Color.WHITE);
//                            slot3.setBackgroundColor(Color.WHITE);
//                            Toast.makeText(avail.this, "This Date is Empty", Toast.LENGTH_SHORT).show();
//                            slot1_selected = "false";
//                            slot2_selected = "false";
//                            slot3_selected = "false";
//                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slot1_selected.equals("true")){
                    slot1_selected="false";
                }
                if(slot1_selected.equals("false")){
                    slot1_selected="true";
                }

//                if(slot1_selected.equals("true")){
//                    slot1.setBackgroundColor(Color.BLUE);
//                }
                if (slot1_selected.equals("false")){
                    slot1.setBackgroundColor(Color.WHITE);
                }
                Toast.makeText(avail.this, "1 = "+slot1_selected+" 2 = "+slot2_selected+" 3 = "+slot3_selected, Toast.LENGTH_SHORT).show();
                alertDialog.show();
                confirm.setText("Are you sure you want to book Slot 1");
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slot1_booking obj1;
                        String email = firebaseUser.getEmail();
                        String val = "true";
                        obj1 = new slot1_booking(val,email);
                        slots+=" 9-12";
                        date=firebaseDatabase.getReference();
                        date.child(current_date).child("slot1").setValue(obj1);
                        alertDialog.dismiss();
                    }
                });

            }

        });
        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slot2_selected.equals("true")){
                    slot2_selected="false";
                }
                if(slot2_selected.equals("false")){
                    slot2_selected="true";
                }
//                if(slot2_selected.equals("true")){
//                    slot2.setBackgroundColor(Color.BLUE);
//                }
                if (slot2_selected.equals("false")){
                    slot2.setBackgroundColor(Color.WHITE);
                }
                Toast.makeText(avail.this, "1 = "+slot1_selected+" 2 = "+slot2_selected+" 3 = "+slot3_selected, Toast.LENGTH_SHORT).show();
                alertDialog.show();
                confirm.setText("Are you sure you want to book Slot 2");
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slot1_booking obj1;
                        TextView confirm = (TextView) mView.findViewById(R.id.confirm_tv);
                        String email = firebaseUser.getEmail();
                        String val = "true";
                        obj1 = new slot1_booking(val,email);
                        slots+=" 12-3";
                        date=firebaseDatabase.getReference();
                        date.child(current_date).child("slot2").setValue(obj1);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slot3_selected.equals("true")){
                    slot3_selected="false";
                }
                if(slot3_selected.equals("false")){
                    slot3_selected="true";
                }

//                if(slot3_selected.equals("true")){
//                    slot3.setBackgroundColor(Color.BLUE);
//                }
                if (slot3_selected.equals("false")){
                    slot3.setBackgroundColor(Color.WHITE);
                }
                Toast.makeText(avail.this, "1 = "+slot1_selected+" 2 = "+slot2_selected+" 3 = "+slot3_selected, Toast.LENGTH_SHORT).show();
                alertDialog.show();
                confirm.setText("Are you sure you want to book Slot 3");
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slot1_booking obj1;
                        TextView confirm = (TextView) mView.findViewById(R.id.confirm_tv);
                        String email = firebaseUser.getEmail();
                        String val = "true";

                        obj1 = new slot1_booking(val,email);
                        slots+=" 3-6";
                        date=firebaseDatabase.getReference();
                        date.child(current_date).child("slot3").setValue(obj1);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.seats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        bookBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                slot1_booking slot1_booking = new slot1_booking(slot1_selected,firebaseUser.getEmail());
//                slot2_booking slot2_booking = new slot2_booking(slot2_selected,firebaseUser.getEmail());
//                slot3_booking slot3_booking = new slot3_booking(slot3_selected,firebaseUser.getEmail());
//                date=firebaseDatabase.getReference();
//                date.child(current_date).child("slot1").setValue(slot1_booking);
//                date.child(current_date).child("slot2").setValue(slot2_booking);
//                date.child(current_date).child("slot3").setValue(slot3_booking);
//                Toast.makeText(avail.this,slot1_selected,Toast.LENGTH_SHORT).show();
//                Toast.makeText(avail.this,slot2_selected,Toast.LENGTH_SHORT).show();
//                Toast.makeText(avail.this,slot3_selected,Toast.LENGTH_SHORT).show();
//                slot1_booking.setStatus("false");
//                slot2_booking.setStatus("false");
//                slot3_booking.setStatus("false");
//            }
//        });

    }
    private void init(){
        calendarView = findViewById(R.id.calendarView);
        slot1 = findViewById(R.id.slot_button1);
        slot2 = findViewById(R.id.slot_button2);
        slot3 = findViewById(R.id.slot_button3);
//        bookBtn = findViewById(R.id.bookBtn);
        slot1_selected="false";
        slot2_selected="false";
        slot3_selected="false";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public String getSlots()
    {
        return slots;
    }

}

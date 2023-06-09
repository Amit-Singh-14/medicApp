package com.example.medmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.medmanager.mydatabase.MedicalDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Expiry extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public RecyclerView expList;
//    public TextView medExpname;
    public ExpiryListAdapter medListAdapter;
    public FloatingActionButton expFab;
    public int user_id;
    Button medTime;
    EditText medName, medQty;
    Switch isRepeat;
    ChipGroup chipGroup;
//    Chip sun,mon,tue,wed,thu,fri,sat;

    int year;
    int month;
    int day;

    String time;

    public MedicalDB DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry);

        DbHelper = MedicalDB.getInstance(getApplicationContext());

        expFab = findViewById(R.id.exp_fab);
        expList = findViewById(R.id.exp_list);


//        medExpname.setText(DbHelper.get(DbHelper.getWritableDatabase(),user_id));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        expList.setLayoutManager(linearLayoutManager);
        medListAdapter = new ExpiryListAdapter(getApplicationContext(), DbHelper);
        medListAdapter.setUserData(DbHelper.getexpiryList(DbHelper.getWritableDatabase()));
        expList.setAdapter(medListAdapter);

        expFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiryAdder().show();
            }
        });
    }
        private AlertDialog expiryAdder(){


            View layout = View.inflate(this, R.layout.add_expiry_dialog, null);
//        medicine details:
            medName = layout.findViewById(R.id.add_med_name);
//            medQty = layout.findViewById(R.id.add_med_qty);
            medTime = layout.findViewById(R.id.add_exp_time);
////        UI components:
//            isRepeat = layout.findViewById(R.id.repeat_switch);
//            chipGroup = layout.findViewById(R.id.chip_group);
//            setChildrenEnabled(chipGroup,false);
//            sun = layout.findViewById(R.id.sunday);
//            mon = layout.findViewById(R.id.monday);
//            tue = layout.findViewById(R.id.tuesday);
//            wed = layout.findViewById(R.id.wednesday);
//            thu = layout.findViewById(R.id.thursday);
//            fri = layout.findViewById(R.id.friday);
//            sat = layout.findViewById(R.id.saturday);

            Calendar calendar = Calendar.getInstance();
            medTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DialogFragment timePicker = new TimePickerFragment();
//                    timePicker.show(getSupportFragmentManager(),"time picker");


                    year = calendar.get(Calendar.YEAR);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    month = calendar.get(Calendar.MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Expiry.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            time = SimpleDateFormat.getDateTimeInstance().format(calendar.getTime());
                        }
                    },year,month,day);

                    datePickerDialog.show();

                }
            });

//            isRepeat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!isRepeat.isChecked()){
//                        setChildrenEnabled(chipGroup,false);
//                    }else{
//                        setChildrenEnabled(chipGroup,true);
//                    }
//                }
//            });


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    String temp = medQty.getText().toString();
//                    int qty = 0;
//                    if (!"". equals(temp))
//                        qty = Integer. parseInt(temp);
//                    String days="0000000";
//                    if(isRepeat.isChecked()){
//                        days = setDaysFormat(sun,mon,tue,wed,thu,fri,sat);
//                    }
                    DbHelper.addexpiry(DbHelper.getWritableDatabase(),medName.getText().toString(), time);
                    medListAdapter.setUserData(DbHelper.getexpiryList(DbHelper.getWritableDatabase()));
                    medListAdapter.notifyDataSetChanged();
                    expList.setAdapter(medListAdapter);

                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setView(layout);
            return builder.create();
        }

        public String setDaysFormat(Chip sun, Chip mon, Chip tue, Chip wed, Chip thu, Chip fri, Chip sat){
            String dayString = ""+ (sun.isChecked()?"1":"0") + (mon.isChecked()?"1":"0") + (tue.isChecked()?"1":"0") + (wed.isChecked()?"1":"0") + (thu.isChecked()?"1":"0") + (fri.isChecked()?"1":"0") + (sat.isChecked()?"1":"0");
            return dayString;
        }

//        public void setChildrenEnabled(ChipGroup chipGroup, Boolean enable) {
//            for(int i=0; i<chipGroup.getChildCount(); i++){
//                chipGroup.getChildAt(i).setEnabled(enable);
//            }
//
//        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            medTime.setText(hourOfDay + ":"+ minute);
        }
    }
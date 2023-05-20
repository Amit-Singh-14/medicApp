package com.example.medmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.mydatabase.MedicalDB;

import java.util.Calendar;

public class ExpiryListAdapter extends RecyclerView.Adapter {
    private Cursor med_list;
    public Context context;
    public MedicalDB helper;
//    public int user_id;

    public ExpiryListAdapter(Context context, MedicalDB helper){
        this.context = context;
        this.helper = helper;
//        this.user_id = user_id;
    }

    public void setUserData(Cursor cursor){
        this.med_list = cursor;
        if(med_list != null)
        {
            med_list.moveToFirst();
        }
        notifyDataSetChanged();
    }



    @NonNull

    @Override
    public ExpiryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expiry_card,parent,false);
        ExpiryHolder vh = new ExpiryHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(med_list != null){
            ((ExpiryHolder) holder).medName.setText(med_list.getString(1));
            ((ExpiryHolder) holder).id = med_list.getInt(0);
//            ((ExpiryHolder) holder).time.setText(med_list.getString(2));

            boolean isChecked = true;
            ((ExpiryHolder) holder).toggleSwitch.setChecked(isChecked);
            ((ExpiryHolder) holder).toggleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((ExpiryHolder) holder).toggleSwitch.isChecked()){
                        helper.setEnable(helper.getWritableDatabase(),((ExpiryHolder) holder).id,1);
                    }else{
                        helper.setEnable(helper.getWritableDatabase(),((ExpiryHolder) holder).id,0);
                    }

                    Cursor c = helper.getExpiry(helper.getWritableDatabase(),((ExpiryHolder) holder).id);
                    c.moveToFirst();
                    String[] raw_time = c.getString(3).split(":",2);
                    int hour = Integer.parseInt(raw_time[0]);
                    int min = Integer.parseInt(raw_time[1]);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);
                    cal.set(Calendar.SECOND,0);

                    Calendar now = Calendar.getInstance();
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);


                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    Intent intent = new Intent(context,AlarmBroadcastReceiver.class);
                    intent.putExtra("medName",c.getString(1));
//                    intent.putExtra("medQty",c.getString(2));
                    intent.putExtra("medTime",c.getString(2));

                    if(((ExpiryHolder) holder).toggleSwitch.isChecked()){
                        //set alarm
                        String days = c.getString(4);
                        if(days.equals("0000000")){
                            if(cal.before(now)){
                                cal.add(Calendar.DATE, 1);
                            }
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(""+((ExpiryHolder) holder).id),intent,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);

                            Toast.makeText(context, "Reminder set for "+c.getString(1)+" on "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE) + ", "+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR), Toast.LENGTH_LONG).show();
                        }
                        else{
                            int ct=1;
                            for(char d : days.toCharArray()){

                                if(d == '1'){
                                    cal.set(Calendar.DAY_OF_WEEK,ct);
                                    if(cal.before(now)){
                                        cal.add(Calendar.DATE, 7);
                                    }

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(""+((ExpiryHolder) holder).id + ""+ct),intent,0);
                                    System.out.println(cal.get(Calendar.DAY_OF_WEEK));
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);
                                }
                                ct++;
                            }
                            Toast.makeText(context, "Reminder set for "+c.getString(1)+" on "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{

                        String days = c.getString(4);
                        if(days.equals("0000000")){
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(""+((ExpiryHolder) holder).id),intent,0);
                            alarmManager.cancel(pendingIntent);
                        }
                        else{
                            int ct=1;
                            for(char d : days.toCharArray()){

                                if(d == '1'){
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(""+((ExpiryHolder) holder).id + ""+ct),intent,0);
                                    alarmManager.cancel(pendingIntent);
                                }
                                ct++;
                            }
                        }

                    }
                }
            });


            ((ExpiryHolder) holder).deleteMed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.deleteEXPIRY(helper.getWritableDatabase(),((ExpiryHolder) holder).id);
                }
            });
            med_list.moveToNext();


        }
    }

    @Override
    public int getItemCount() {
        return med_list.getCount();
    }




    public class ExpiryHolder extends RecyclerView.ViewHolder{
        TextView medName, time;
        ImageButton deleteMed;
        int id;
        Switch toggleSwitch;


        public ExpiryHolder(@NonNull View itemView) {
            super(itemView);
            medName = (TextView) itemView.findViewById(R.id.med_name);
            time = (TextView) itemView.findViewById(R.id.exp_time);
            deleteMed = (ImageButton) itemView.findViewById(R.id.delete_med);
            toggleSwitch = (Switch) itemView.findViewById(R.id.toggle_switch);
        }
    }


}

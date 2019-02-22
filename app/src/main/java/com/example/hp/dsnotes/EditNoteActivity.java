package com.example.hp.dsnotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    static int[] arr;
    private EditText editText,notification;
    private Button edittime,editdate;

    Button selecttime;

    int yr,mnt,day;


    private NoteDao dao;
    private Note temp;
    public static final String keyValue = "note_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        arr=new int[1000];

        //code for onBackpressed button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //end of the permission code
        if(!chkshared())
        Arrays.fill(arr,-1);
        saveshared();

        editText = findViewById(R.id.input_note);
        dao = NoteDb.getInstance(this).noteDao();

        if(getIntent().getExtras()!= null) {

            int id = getIntent().getExtras().getInt(keyValue, 0);
            temp = dao.getNoteById(id);
            editText.setText(temp.getNoteText());

        }
        else temp = new Note();



    }

    private void saveshared() {
        SharedPreferences sharedPreferences = getSharedPreferences("Infos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("value","dekha");
        editor.commit();
    }

    private boolean chkshared() {
        SharedPreferences sharedPreferences = getSharedPreferences("Infos", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("value","").equals("dekha"))return true;
        else return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.saveId){

            onSaveNote();
        }
        if(item.getItemId()==android.R.id.home){

            // code for working back button
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void onSaveNote() {

        String text = editText.getText().toString();


        if(!text.isEmpty()){

            long date = new Date().getTime(); //get current time
           // check if note exist than edit otherwise insert

            temp.setNoteText(text);
            temp.setNoteDate(date);

            if(temp.getId() > -1)
                dao.insertData(temp); //input them into database
            else
                dao.updateData(temp);

            showdialog();

        }else{
            Toast.makeText(this, "Notes are Empty! Write something", Toast.LENGTH_LONG).show();
        }
    }

    private void showdialog() {

        final AlertDialog.Builder alertDialogBuidler = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogue, (ViewGroup) findViewById(R.id.dlayout));

        edittime=view.findViewById(R.id.selecttime);
        editdate=view.findViewById(R.id.selectdate);

        notification=view.findViewById(R.id.dedit);

        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show1();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
        alertDialogBuidler.setCancelable(false);
        alertDialogBuidler.setView(view);

        final AlertDialog alertDialog = alertDialogBuidler.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (chkvalidity()) {

                    setnotification();
                    alertDialog.dismiss();
                }
                else Toast.makeText(EditNoteActivity.this, "All field must have to filled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void show1() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.ghori, null);
        final DatePicker dt=view.findViewById(R.id.datepicker);
       // final TimePicker tm = view.findViewById(R.id.timee);

        android.app.AlertDialog.Builder alertDialogBuidler = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuidler.setView(view);
        alertDialogBuidler.setCancelable(false)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    yr=dt.getYear();
                     mnt=dt.getMonth();
                     day=dt.getDayOfMonth();
                    String ss=yr+"/"+(mnt+1)+"/"+day;
                    editdate.setText(ss);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    private boolean chkvalidity() {
        if(!editdate.getText().toString().isEmpty()&&!edittime.getText().toString().isEmpty()&&!notification.getText().toString().isEmpty()){
            return true;
        }
        else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setnotification() {
        String s=edittime.getText().toString();
        String p=editdate.getText().toString();
        int hr=Integer.parseInt(s.substring(0,2));
        int mn=Integer.parseInt(s.substring(3,5));
        String peram=s.substring(6,8);
        if(peram.equals("AM")&&hr==12){
            hr=0;
        }
        if(peram.equals("PM")&&hr!=12){
            hr+=12;
        }

            Calendar calender = Calendar.getInstance();
        calender.set(Calendar.YEAR,yr);
        calender.set(Calendar.MONTH,mnt);
        calender.set(Calendar.DAY_OF_MONTH,day);
            calender.set(Calendar.HOUR_OF_DAY, hr);  //pass hour which you have select
            calender.set(Calendar.MINUTE, mn);  //pass min which you have select
            calender.set(Calendar.SECOND, 0);
            calender.set(Calendar.MILLISECOND, 0);

            Calendar now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);

            if (calender.before(now)) {    //past alarm jatey fire na oyy
                calender.add(Calendar.DATE, 7);
            }
            Intent intent=new Intent(getApplicationContext(),Setbroadcast.class);
            intent.putExtra("notifi",notification.getText().toString());
            int id=getid();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),(int)System.currentTimeMillis()+(int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
            AlarmManager mgrAlarm;
            mgrAlarm= (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                mgrAlarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                // Log.e("Marshmello",hr+" marsh "+min);
            } else {
                mgrAlarm.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                // Log.e(" not Marshmello",hr+" not marsh "+min );

            }
        finish(); //return to  Mainactivity


    }

    private int getid() {
        int t=0;
        for(int i=1;i<=800;i++){
            if(arr[i]==-1){
                t=i;
                arr[i]=1;
                break;
            }
        }
        return t;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savedialogue(View v){
        if(chkvalidity()){
            setnotification();
        }
        else Toast.makeText(this, "Please fill up all field", Toast.LENGTH_SHORT).show();

    }

    public void show(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.timepickerr, null);
        final TimePicker tm = view.findViewById(R.id.timee);

        android.app.AlertDialog.Builder alertDialogBuidler = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuidler.setView(view);
        alertDialogBuidler.setCancelable(false)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ttlTime = "";
                        int hour = tm.getCurrentHour();
                        int minutes = tm.getCurrentMinute();
                        String perams = "AM";
                        if (hour == 0) {
                            hour = hour + 12;
                        } else if (hour >= 12) {
                            if (hour != 12) hour = hour - 12;
                            perams = "PM";
                        }

                        if (hour < 10) ttlTime += "0" + hour + ":";
                        else ttlTime += hour + ":";

                        if (minutes < 10) ttlTime += "0" + minutes;
                        else ttlTime += minutes;

                        ttlTime += " " + perams;

                        edittime.setText(ttlTime);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }




}

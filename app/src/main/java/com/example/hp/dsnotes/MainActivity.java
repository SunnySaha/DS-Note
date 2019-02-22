package com.example.hp.dsnotes;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotchemi.android.rate.AppRate;

import static com.example.hp.dsnotes.EditNoteActivity.keyValue;


public class MainActivity extends AppCompatActivity implements EventListener {

    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<Note> notes;
    NoteAdapter noteAdapter;
    private NoteDao dao;
    private Note note;
    private EventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //codes for navigation drawer

        drawerLayout = findViewById(R.id.drawyer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.nav_open, R.string.nav_closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feedbackId:


                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.feedback);

                        final EditText name = dialog.findViewById(R.id.editnameId);
                        final EditText message = dialog.findViewById(R.id.editmessageId);
                        ImageView canceldialog = (ImageView) dialog.findViewById(R.id.canceldialog);
                        Button send = dialog.findViewById(R.id.sendfeedback);

                        dialog.setCancelable(false);

                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!name.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {

                                    String text = name.getText().toString();
                                    String feedback = message.getText().toString();
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822"); // remember to open email apps
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"devashisdas26@gmail.com"});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback From Ds Note App");
                                    intent.putExtra(Intent.EXTRA_TEXT, "Name: " + text + "\n Message: " + feedback);
                                    startActivity(Intent.createChooser(intent, "Choose app to send Email"));
                                } else {
                                    Toast.makeText(MainActivity.this, "You should fillup all the fields", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        canceldialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });



                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
//permission if
                        break;
                }
                //end of code of feedback

                switch (item.getItemId()) {
                    case R.id.shareId:

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plane");
                        String subject = "DS Note";
                        String body = "It will help you to create NOTES according your choice. It will also notify you about NOTES.\n Let's use DS Note.\ncom.example.hp.dsnotes";
                        i.putExtra(Intent.EXTRA_SUBJECT, subject);
                        i.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(i, "Share With"));
                        break;
                }
                //end of code of shareId
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //end of code of navigation drawer



        //codes for rating

        AppRate.with(this)
                .setInstallDays(1) // show rating bar after days of install
                .setLaunchTimes(2) // show when the app run one times
                .setRemindInterval(3) // if the user select remind me later than it show agian after given days
                .monitor(); // to monitor the functions

        AppRate.showRateDialogIfMeetsConditions(this); // to show the dialog box according to monitor
        AppRate.with(this).clearAgreeShowDialog();


        //end of code for rating

        //code for fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addNewNote();
            }
        });
        dao = NoteDb.getInstance(this).noteDao();

        //end of the code for fab button
    }

    private void LoadNotes() {

        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes(); //load all notes from database
        this.notes.addAll(list); // add all notes

        this.noteAdapter = new NoteAdapter(notes, this);
        this.noteAdapter.setListener(this);
        this.recyclerView.setAdapter(noteAdapter);


    }

    private void addNewNote() {

        Intent i = new Intent(MainActivity.this, EditNoteActivity.class);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void letsPermission() {

        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET);
    }


        @Override
        protected void onResume(){
            super.onResume();
            LoadNotes();
        }








    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(keyValue, note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(final Note note) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setCancelable(false)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dao.deleteData(note); //delete note
                        LoadNotes(); //refresh note after delete
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String subject = note.getNoteText() + "\n Created on Date: " + dataUtil.DateFromLong(note.getNoteDate());
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_TEXT, subject);
                        startActivity(intent);


                    }
                })
                .create()
                .show();
    }
}

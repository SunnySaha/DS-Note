package com.example.hp.dsnotes;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>{


     ArrayList<Note>notes;
     Context context;
    private EventListener listener;

    public NoteAdapter(ArrayList<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myview = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        final Note note = getNote(position);
        if(note!=null){
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(dataUtil.DateFromLong(note.getNoteDate()));

            // in their noteClick will perform
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note);
                }
            });
            // here noteLongClick will perform

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
        }
    }

    private Note getNote(int position){
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

         TextView noteText, noteDate;

        public NoteHolder(View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.textviewId);
            noteDate = itemView.findViewById(R.id.dateviewId);


        }
    }

    public void setListener(EventListener listener){

        this.listener = listener;
    }


}

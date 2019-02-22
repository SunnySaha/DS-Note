package com.example.hp.dsnotes;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = Note.class, version = 1)
public abstract class NoteDb extends RoomDatabase {

    public abstract NoteDao noteDao();
    private static NoteDb instance;
    public static final String DATABASE_NAME = "noteDb";
    public static NoteDb getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context, NoteDb.class, DATABASE_NAME ).allowMainThreadQueries().build();
        }
        return instance;
    }

}

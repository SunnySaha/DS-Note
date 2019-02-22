package com.example.hp.dsnotes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //if the data exist than replate it
    void insertData(Note note);

    @Delete
    void deleteData(Note note);

    @Update
    void updateData(Note note);

    @Query("SELECT * FROM notes") //list of all from database
    List<Note> getNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId") // list get by id
    Note getNoteById(int noteId);

  @Query("SELECT * FROM notes WHERE id = :noteId") //delete item by id
     Note deleteNoteById(int noteId);
}

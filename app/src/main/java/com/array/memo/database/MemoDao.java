package com.array.memo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.array.memo.models.Memo;

import java.util.List;


@Dao
public interface MemoDao {
    /**
     * Insert and save memo to Database
     *
     * @param memo
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Memo memo);

    /**
     * Delete memo
     *
     * @param memo that will be delete
     */
    @Delete
    void deleteNote(Memo... memo);

    /**
     * Update memo
     *
     * @param memo the memo that will be update
     */
    @Update
    void updateNote(Memo memo);

    /**
     * List All Notes From Database
     *
     * @return list of Notes
     */
    @Query("SELECT * FROM memo")
    List<Memo> getNotes();

    /**
     * @param noteId note id
     * @return Memo
     */
    @Query("SELECT * FROM memo WHERE id = :noteId")
    Memo getNoteById(int noteId);

    /**
     * Delete Memo by Id from DataBase
     *
     * @param noteId
     */
    @Query("DELETE FROM memo WHERE id = :noteId")
    void deleteNoteById(int noteId);

}

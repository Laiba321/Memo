package com.array.memo.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.array.memo.models.Memo;

@Database(entities = Memo.class, version = 1)
public abstract class MemoDataBase extends RoomDatabase {
    public abstract MemoDao notesDao();

    public static final String DATABSE_NAME = "notesDb";
    private static MemoDataBase instance;

    public static MemoDataBase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, MemoDataBase.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}

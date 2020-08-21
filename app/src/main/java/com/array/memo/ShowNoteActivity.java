package com.array.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.array.memo.database.MemoDataBase;
import com.array.memo.database.MemoDao;
import com.array.memo.models.Memo;
import com.array.memo.utilities.MemoUtilities;


public class ShowNoteActivity extends AppCompatActivity {

    private TextView showtitle,showtext,showdate;
    private MemoDao dao;
    private Memo temp;
    public static final String NOTE_EXTRA_Key = "note_id";

    private MenuItem backItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        Toolbar toolbar = findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);


        showtitle = findViewById(R.id.show_title);
        showtext = findViewById(R.id.show_text);
        showdate = findViewById(R.id.show_date);
        dao = MemoDataBase.getInstance(this).notesDao();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            showtitle.setText(temp.getNoteTitle());
            showtext.setText(temp.getNoteText());
            showdate.setText(MemoUtilities.dateFromLong(temp.getNoteDate()));


        } else showtext.setFocusable(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.back_note: //cancel the note
                actionCancel();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void actionCancel() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
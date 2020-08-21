package com.array.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.array.memo.models.Memo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.array.memo.adapter.MemoAdapter;
import com.array.memo.callbackMethods.MainActionCallbackMethod;
import com.array.memo.callbackMethods.MemoEventListener;
import com.array.memo.database.MemoDataBase;
import com.array.memo.database.MemoDao;
import com.array.memo.utilities.MemoUtilities;


import java.util.ArrayList;
import java.util.List;

import static com.array.memo.EditNoteActivity.NOTE_EXTRA_Key;

public class MainActivity extends AppCompatActivity implements MemoEventListener {
    private MenuItem addItem;
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Memo> memos;
    private MemoAdapter adapter;
    private MemoDao dao;
    private MainActionCallbackMethod actionModeCallback;
    private int chackedCount = 0;
    private FloatingActionButton fab;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dao = MemoDataBase.getInstance(this).notesDao();

    }



    private void loadNotes() {
        this.memos = new ArrayList<>();
        List<Memo> list = dao.getNotes();// get All memos from DataBase
        this.memos.addAll(list);
        this.adapter = new MemoAdapter(this, this.memos);

        // set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();



    }

    // no memos show msg in main_activity

    private void showEmptyView() {
        if (memos.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }


    //Start EditNoteActivity.class for add New Memo

    private void onAddNewNote() {
        startActivity(new Intent(this, EditNoteActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        addItem = menu.findItem(R.id.action_add);
        return true;
    }

    //menu item is selected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add: //add the note
                onAddNewNote();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    //show the details of memo when click on memos
    public void onNoteClick(Memo memo) {

        Intent edit = new Intent(this, ShowNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, memo.getId());
        startActivity(edit);


    }

    //show menu items share edit and delete when long click on memos

    @Override
    public void onNoteLongClick(Memo memo) {

        memo.setChecked(true);
        chackedCount = 1;
        adapter.setMultiCheckMode(true);



        adapter.setListener(new MemoEventListener() {

            @Override
            public void onNoteClick(Memo memo) {
                memo.setChecked(!memo.isChecked()); // inverse selected
                if (memo.isChecked())
                    chackedCount++;
                else chackedCount--;
                if (chackedCount > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                } else actionModeCallback.changeShareItemVisible(true);

                if (chackedCount > 1) {
                    actionModeCallback.changeEditItemVisible(false);
                } else actionModeCallback.changeEditItemVisible(true);


                if (chackedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    actionModeCallback.getAction().finish();
                }



                if (chackedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    actionModeCallback.getAction().finish();
                }

                actionModeCallback.setCount(chackedCount + "/" + memos.size());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNoteLongClick(Memo memo) {


            }

        });

        actionModeCallback = new MainActionCallbackMethod() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNote();
                else if (menuItem.getItemId() == R.id.action_edit_note)
                    onEditNote();

                actionMode.finish();
                return false;
            }

        };

        // start action mode
        startActionMode(actionModeCallback);
        // hide add button
        addItem.setVisible(false);
        actionModeCallback.setCount(chackedCount + "/" + memos.size());
    }

    //share action on long click
    private void onShareNote() {


        Memo memo = adapter.getCheckedNotes().get(0);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = memo.getNoteTitle()+ "\n "+ memo.getNoteText() + "\n Created on: " +
                MemoUtilities.dateFromLong(memo.getNoteDate()) + "\n  By :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);


    }

    //edit action on long click
    private void onEditNote() {

        Memo memo = adapter.getCheckedNotes().get(0);

        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, memo.getId());
        startActivity(edit);


    }

    //delete action on long click
    private void onDeleteNotes() {


        List<Memo> chackedMemos = adapter.getCheckedNotes();
        if (chackedMemos.size() != 0) {
            for (Memo memo : chackedMemos) {
                dao.deleteNote(memo);
            }
            // refresh Notes
            loadNotes();
            Toast.makeText(this, chackedMemos.size() + " Delete successfully !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Memo is selected", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false); // uncheck the memos
        adapter.setListener(this); // set back the old listener
        addItem.setVisible(true);  //visible the add item
    }



}






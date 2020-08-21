package com.array.memo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.array.memo.R;
import com.array.memo.callbackMethods.MemoEventListener;
import com.array.memo.models.Memo;
import com.array.memo.utilities.MemoUtilities;

import java.util.ArrayList;
import java.util.List;


public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.NoteHolder> {
    private Context context;
    private ArrayList<Memo> memos;
    private MemoEventListener listener;
    private boolean multiCheckMode = false;


    public MemoAdapter(Context context, ArrayList<Memo> memos) {
        this.context = context;
        this.memos = memos;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        final Memo memo = getNote(position);
        if (memo != null) {
            holder.noteTitle.setText(memo.getNoteTitle());
            // holder.noteText.setText(memo.getNoteText());
            holder.noteDate.setText(MemoUtilities.dateFromLong(memo.getNoteDate()));
            // init memo click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(memo);
                }
            });

            // init memo long click
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onNoteLongClick(memo);
                    return false;
                }
            });

            // check checkBox if memo selected
            if (multiCheckMode) {
                holder.checkBox.setVisibility(View.VISIBLE); // show checkBox if multiMode on
                holder.checkBox.setChecked(memo.isChecked());
            } else holder.checkBox.setVisibility(View.GONE); // hide checkBox if multiMode off


        }
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    private Memo getNote(int position) {
        return memos.get(position);
    }



    public List<Memo> getCheckedNotes() {
        List<Memo> checkedMemos = new ArrayList<>();
        for (Memo n : this.memos) {
            if (n.isChecked())
                checkedMemos.add(n);
        }

        return checkedMemos;
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        TextView   noteTitle , noteDate;
        CheckBox checkBox;

        public NoteHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDate = itemView.findViewById(R.id.note_date);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    public void setListener(MemoEventListener listener) {
        this.listener = listener;
    }

    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        if (!multiCheckMode)
            for (Memo memo : this.memos) {
                memo.setChecked(false);
            }
        notifyDataSetChanged();
    }
}

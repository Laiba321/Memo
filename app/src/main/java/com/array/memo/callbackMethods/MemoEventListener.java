package com.array.memo.callbackMethods;

import com.array.memo.models.Memo;


public interface MemoEventListener {
    /**
     * call wen memo clicked.
     *
     * @param memo: memo item
     */
    void onNoteClick(Memo memo);

    /**
     * call wen long Click to memo.
     *
     * @param memo : item
     */
    void onNoteLongClick(Memo memo);
}

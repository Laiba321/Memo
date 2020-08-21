package com.array.memo.callbackMethods;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.array.memo.R;

public abstract class MainActionCallbackMethod implements ActionMode.Callback {
    private ActionMode action;
    private MenuItem countItem;
    private MenuItem shareItem;
    private MenuItem editItem;

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.main_action_mode, menu);
        this.action = actionMode;
        this.countItem = menu.findItem(R.id.action_checked_count);
        this.shareItem = menu.findItem(R.id.action_share_note);
        this.editItem = menu.findItem(R.id.action_edit_note);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

    public void setCount(String checkedCount) {
        if (countItem != null)
            this.countItem.setTitle(checkedCount);
    }

    public void changeShareItemVisible(boolean b) {
        shareItem.setVisible(b);
    }

    public void changeEditItemVisible(boolean b) {
        editItem.setVisible(b);
    }


    public ActionMode getAction() {
        return action;
    }
}

package ru.spb.itolia.perashki.ui;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import ru.spb.itolia.perashki.R;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 17.10.12
 * Time: 0:08
 */
public class FilterActivity extends SherlockPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.filters);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

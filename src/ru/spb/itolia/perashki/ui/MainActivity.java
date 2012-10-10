package ru.spb.itolia.perashki.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.TabsAdapter;
import ru.spb.itolia.perashki.beans.piroType;
import ru.spb.itolia.perashki.util.IShowedFragment;

public class MainActivity extends SherlockFragmentActivity {
    private static final String TAG = "Perashki.MainActivity";
    private ViewPager pager;
    private TitlePageIndicator indicator;
    private TabsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adapter = new TabsAdapter(getSupportFragmentManager(), false);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
/*        adapter.addFragment(new PiroListFragment(piroType.NEW), getResources().getString(R.string.new_tab_title));
        Log.v(TAG, "Added new piros fragment");*/  //New piros only available if logged in
        adapter.addFragment(new PiroListFragment(piroType.GOOD), getResources().getString(R.string.good_tab_title));
        Log.v(TAG, "Added good piros fragment");
        adapter.addFragment(new PiroListFragment(piroType.BEST), getResources().getString(R.string.best_tab_title));
        Log.v(TAG, "Added second fragment");
        //adapter.addFragment(new PiroListFragment(piroType.RANDOM), getResources().getString(R.string.random_tab_title));
        //Log.v(TAG, "Added random fragment");
        adapter.addFragment(new PiroListFragment(piroType.ALL), getResources().getString(R.string.all_tab_title));
        Log.v(TAG, "Added archive fragment");
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                Log.v(TAG, "onPageSelected");
                PiroListFragment fragment = (PiroListFragment) adapter.instantiateItem(pager, i);
                if (fragment instanceof IShowedFragment) {
                    fragment.onShowedFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.string.settings_label, Menu.NONE, getString(R.string.settings_label))
                .setIcon(R.drawable.actionbar_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        switch (selectedItemId) {
            case R.string.settings_label:
                Intent mIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(mIntent, 0);
                break;
        }
        return false;
    }


}

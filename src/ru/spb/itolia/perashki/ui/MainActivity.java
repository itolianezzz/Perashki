package ru.spb.itolia.perashki.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.TabsAdapter;
import ru.spb.itolia.perashki.beans.piroType;

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
        adapter.addFragment(new PiroListFragment(piroType.RANDOM), getResources().getString(R.string.random_tab_title));
        Log.v(TAG, "Added random fragment");
        adapter.addFragment(new PiroListFragment(piroType.ALL), getResources().getString(R.string.all_tab_title));
        Log.v(TAG, "Added archive fragment");
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                Log.v(TAG, "onPageSelected");
                //PiroListFragment currentFragment = (PiroListFragment) adapter.getItem(i);
                //currentFragment.populateView();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        //PiroListFragment currentFragment = (PiroListFragment) adapter.getItem(0);
        //currentFragment.populateView();
    }

}

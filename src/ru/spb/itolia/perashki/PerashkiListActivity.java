package ru.spb.itolia.perashki;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;
import ru.spb.itolia.perashki.adapters.TabsAdapter;
import ru.spb.itolia.perashki.ui.PiroListFragment;

public class PerashkiListActivity extends SherlockFragmentActivity {
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
        adapter.addFragment(new PiroListFragment(PiroLoader.PIRO_GOOD), "good"); //TODO Change for normal text
        adapter.addFragment(new PiroListFragment(PiroLoader.PIRO_BEST), "best");
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                PiroListFragment currentFragment = (PiroListFragment) adapter.getItem(i);
                    currentFragment.populateView();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        adapter.notifyDataSetChanged();
        //PiroListFragment currentFragment = (PiroListFragment) adapter.getItem(0);
        //currentFragment.populateView();
    }

}

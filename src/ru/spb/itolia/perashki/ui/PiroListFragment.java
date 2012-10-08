package ru.spb.itolia.perashki.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.PiroLoader;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.Piro;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 06.10.12
 * Time: 15:24
 */
public class PiroListFragment extends SherlockFragment {
    private ProgressBar progress;
    private ListView list;
    private int piroType = 1;

    public PiroListFragment(){
        }

    public PiroListFragment(int piroType) {
            this.piroType = piroType;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piro_list_view, container, false);
        list = (ListView) view.findViewById(R.id.piro_list);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    public void populateView(){
        LoadPirosTask pirosTask = new LoadPirosTask();
        pirosTask.execute();
    }

    private class LoadPirosTask extends AsyncTask<Integer , Void, List<Piro>> {


        protected void onPreExecute(){
            progress.setIndeterminate(true);
            //dialog = ProgressDialog.show(getSherlockActivity(), "", "Loading. Please wait...", true);  //TODO Define string in strings.xml
        }

        @Override
        protected List<Piro> doInBackground(Integer... params) {
            List<Piro> piros = null;
            try {
                switch (piroType){
                    case 1:
                        piros = PiroLoader.getGood();
                    case 2:
                        piros = PiroLoader.getBest();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return piros;
        }

        @Override
        protected void onPostExecute(List<Piro> piros){
            progress.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            PiroAdapter mAdapter = new PiroAdapter(getSherlockActivity(), piros);
            list.setAdapter(mAdapter);

        }
    }

}
